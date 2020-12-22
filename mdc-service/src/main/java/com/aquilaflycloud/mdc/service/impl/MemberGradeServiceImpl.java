package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import com.aquilaflycloud.mdc.mapper.MemberGradeMapper;
import com.aquilaflycloud.mdc.model.member.MemberGrade;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.result.member.MemberGradeResult;
import com.aquilaflycloud.mdc.service.ClientConfigService;
import com.aquilaflycloud.mdc.service.MemberGradeService;
import com.aquilaflycloud.mdc.service.MemberRewardService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MemberGradeServiceImpl
 *
 * @author star
 * @date 2020-03-05
 */
@Slf4j
@Service
public class MemberGradeServiceImpl implements MemberGradeService {
    @Resource
    private MemberGradeMapper memberGradeMapper;
    @Resource
    private MemberRewardService memberRewardService;
    @Resource
    private ClientConfigService clientConfigService;

    @Override
    public List<MemberGradeResult> listMemberGrade(MemberGradeListParam param) {
        return memberGradeMapper.selectList(Wrappers.<MemberGrade>lambdaQuery()
                .eq(param.getRewardType() != null, MemberGrade::getRewardType, param.getRewardType())
                .eq(StrUtil.isNotBlank(param.getAppId()), MemberGrade::getAppId, param.getAppId())
                .orderByDesc(MemberGrade::getRewardType).orderByAsc(MemberGrade::getGradeOrder)
        ).stream().map(grade -> {
            MemberGradeResult result = new MemberGradeResult();
            BeanUtil.copyProperties(grade, result);
            String key = memberRewardService.getRankCacheKey(null, null, grade.getAppId(), grade.getRewardType());
            Long count = RedisUtil.<Long>zSetRedis().count(key, grade.getMinValue(), grade.getMaxValue() + 1);
            result.setMemberCount(count);
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public MemberGrade getMemberGrade(MemberGradeGetParam param) {
        return memberGradeMapper.selectById(param.getId());
    }

    private MemberGrade[] getGradeCache(String appId, RewardTypeEnum rewardType, Boolean... reload) {
        String key = StrUtil.join("_", "gerGradeList", rewardType.getType(), appId, MdcUtil.getCurrentTenantId());
        RedisUtil.redis().delete(key);
        RLocalCachedMap<String, MemberGrade[]> localCachedMap = RedisUtil.redisson().getLocalCachedMap("gradeCacheMap", LocalCachedMapOptions.defaults());
        if (reload.length > 0 && reload[0]) {
            localCachedMap.remove(key);
        }
        if (!localCachedMap.containsKey(key)) {
            MemberGrade[] grades = memberGradeMapper.selectList(Wrappers.<MemberGrade>lambdaQuery()
                    .eq(MemberGrade::getRewardType, rewardType)
                    .eq(MemberGrade::getAppId, appId)
                    .orderByAsc(MemberGrade::getGradeOrder)
            ).toArray(new MemberGrade[]{});
            if (ArrayUtil.isEmpty(grades)) {
                MemberGrade grade = new MemberGrade();
                ArrayUtil.append(grades, grade);
            }
            localCachedMap.put(key, grades);
        }
        return localCachedMap.get(key);
    }

    @Transactional
    @Override
    public void batchAddMemberGrade(MemberGradeBatchAddParam param) {
        memberGradeMapper.delete(Wrappers.<MemberGrade>lambdaQuery()
                .eq(MemberGrade::getAppId, param.getAppId())
                .eq(MemberGrade::getRewardType, param.getRewardType())
        );
        CollUtil.sort(param.getMemberGradeInfoList(), Comparator.comparing(MemberGradeBatchAddParam.MemberGradeInfo::getMinValue));
        List<MemberGrade> gradeList = new ArrayList<>();
        int i = 1;
        for (MemberGradeBatchAddParam.MemberGradeInfo memberGradeInfo : param.getMemberGradeInfoList()) {
            MemberGrade grade = new MemberGrade();
            BeanUtil.copyProperties(memberGradeInfo, grade);
            grade.setAppId(param.getAppId());
            grade.setRewardType(param.getRewardType());
            grade.setGradeOrder(i);
            i++;
            gradeList.add(grade);
        }
        int count = memberGradeMapper.insertAllBatch(gradeList);
        if (count <= 0) {
            throw new ServiceException("批量新增失败");
        }
        getGradeCache(param.getAppId(), param.getRewardType(), true);
    }

    @Override
    public void addMemberGrade(MemberGradeAddParam param) {
        MemberGrade grade = new MemberGrade();
        BeanUtil.copyProperties(param, grade);
        int count = memberGradeMapper.insert(grade);
        if (count <= 0) {
            throw new ServiceException("新增会员等级失败");
        }
        getGradeCache(param.getAppId(), param.getRewardType(), true);
    }

    @Override
    public void editMemberGrade(MemberGradeEditParam param) {
        MemberGrade grade = memberGradeMapper.selectById(param.getId());
        if (grade == null) {
            throw new ServiceException("会员等级不存在");
        }
        MemberGrade update = new MemberGrade();
        BeanUtil.copyProperties(param, update);
        int count = memberGradeMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("编辑会员等级失败");
        }
        getGradeCache(grade.getAppId(), grade.getRewardType(), true);
    }

    @Override
    public void deleteMemberGrade(MemberGradeGetParam param) {
        MemberGrade grade = memberGradeMapper.selectById(param.getId());
        if (grade == null) {
            throw new ServiceException("会员等级不存在");
        }
        int count = memberGradeMapper.deleteById(param.getId());
        if (count <= 0) {
            throw new ServiceException("删除会员等级失败");
        }
        getGradeCache(grade.getAppId(), grade.getRewardType(), true);
    }

    @Override
    public MemberGrade getRewardGrade(String appId, RewardTypeEnum rewardType, Integer reward) {
        MemberGrade[] grades = getGradeCache(appId, rewardType);
        MemberGrade defaultGrade = new MemberGrade();
        defaultGrade.setGradeTitle(clientConfigService.getItemByName(appId, "defaultGradeTitle"));
        defaultGrade.setMinValue(0);
        defaultGrade.setMaxValue(0);
        if (grades.length > 0) {
            if (grades[0].getId() == null) {
                return defaultGrade;
            }
            if (reward < grades[0].getMinValue()) {
                return grades[0];
            } else if (reward > grades[grades.length - 1].getMaxValue()) {
                return grades[grades.length - 1];
            } else {
                for (MemberGrade grade : grades) {
                    if (grade.getMinValue() <= reward && reward <= grade.getMaxValue()) {
                        return grade;
                    }
                }
            }
        }
        return defaultGrade;
    }
}
