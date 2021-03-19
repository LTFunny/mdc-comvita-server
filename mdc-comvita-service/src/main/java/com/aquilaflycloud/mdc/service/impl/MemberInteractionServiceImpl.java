package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.BooleanUtil;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.mapper.MemberInteractionMapper;
import com.aquilaflycloud.mdc.model.member.MemberInteraction;
import com.aquilaflycloud.mdc.param.member.MemberInteractionPageParam;
import com.aquilaflycloud.mdc.param.member.MemberInteractionParam;
import com.aquilaflycloud.mdc.result.member.MemberInteractionResult;
import com.aquilaflycloud.mdc.service.MemberInteractionService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import com.gitee.sop.servercommon.param.ServiceParamValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * MemberInteractionServiceImpl
 *
 * @author star
 * @date 2021/3/18
 */
@Service
@Slf4j
public class MemberInteractionServiceImpl implements MemberInteractionService {
    @Resource
    private MemberInteractionMapper memberInteractionMapper;

    private final ServiceParamValidator serviceParamValidator = new ServiceParamValidator();

    private void saveRecord(MemberInteractionParam param, Boolean isCancel) {
        Long memberId = MdcUtil.getCurrentMemberId();
        RedisUtil.syncLoad("saveInteractionLock" + memberId, () -> {
            int count = memberInteractionMapper.selectCount(Wrappers.<MemberInteraction>lambdaQuery()
                    .eq(MemberInteraction::getBusinessId, param.getBusinessId())
                    .eq(MemberInteraction::getInteractionType, param.getInteractionType())
                    .eq(MemberInteraction::getMemberId, memberId)
            );
            MemberInteraction memberInteraction = new MemberInteraction();
            if (count > 0) {
                memberInteraction.setInteractionTime(DateTime.now());
                memberInteraction.setIsCancel(isCancel ? WhetherEnum.YES : WhetherEnum.NO);
                memberInteractionMapper.update(memberInteraction, Wrappers.<MemberInteraction>lambdaUpdate()
                        .eq(MemberInteraction::getBusinessId, param.getBusinessId())
                        .eq(MemberInteraction::getInteractionType, param.getInteractionType())
                        .eq(MemberInteraction::getMemberId, memberId)
                );
            } else {
                BeanUtil.copyProperties(param, memberInteraction);
                memberInteraction.setMemberId(memberId);
                memberInteraction.setInteractionTime(DateTime.now());
                memberInteraction.setIsCancel(isCancel ? WhetherEnum.YES : WhetherEnum.NO);
                memberInteractionMapper.insert(memberInteraction);
            }
            return null;
        });
    }

    private boolean loadNum(MemberInteractionParam param) {
        String lockName = "interactionNumLock_" + param.getBusinessType() + param.getInteractionType() + param.getBusinessId();
        String key = "interactionNum_" + param.getBusinessType() + param.getInteractionType() + param.getBusinessId();
        if (!BooleanUtil.isTrue(RedisUtil.redis().hasKey(key))) {
            return RedisUtil.syncLoad(lockName, () -> {
                List<MemberInteraction> list = memberInteractionMapper.selectList(Wrappers.<MemberInteraction>lambdaQuery()
                        .eq(MemberInteraction::getBusinessType, param.getBusinessType())
                        .eq(MemberInteraction::getInteractionType, param.getInteractionType())
                        .eq(MemberInteraction::getBusinessId, param.getBusinessId())
                        .eq(MemberInteraction::getIsCancel, WhetherEnum.NO)
                        .groupBy(MemberInteraction::getMemberId)
                        .orderByDesc(MemberInteraction::getInteractionTime)
                );
                if (list.size() > 0) {
                    for (MemberInteraction interaction : list) {
                        RedisUtil.hashRedis().put(key, interaction.getMemberId(), true);
                    }
                } else {
                    RedisUtil.hashRedis().put(key, "000", true);
                }
                RedisUtil.redis().expire(key, 7, TimeUnit.DAYS);
                return true;
            });
        } else {
            RedisUtil.redis().expire(key, 7, TimeUnit.DAYS);
            return true;
        }
    }

    @Override
    public Long getInteractionNum(MemberInteractionParam param) {
        serviceParamValidator.validateBizParam(param);
        boolean flag = loadNum(param);
        if (flag) {
            String key = "interactionNum_" + param.getBusinessType() + param.getInteractionType() + param.getBusinessId();
            Long number = RedisUtil.hashRedis().size(key);
            if (RedisUtil.hashRedis().get(key, "000") != null) {
                number--;
            }
            return Convert.toLong(number, 0L);
        }
        throw new ServiceException("获取互动数失败");
    }

    @Override
    public Long toggleInteractionNum(MemberInteractionParam param) {
        long num = getInteractionNum(param);
        Long memberId = MdcUtil.getCurrentMemberId();
        if (memberId != null) {
            String key = "interactionNum_" + param.getBusinessType() + param.getInteractionType() + param.getBusinessId();
            if (RedisUtil.hashRedis().hasKey(key, memberId)) {
                RedisUtil.hashRedis().delete(key, memberId);
                num--;
                saveRecord(param, true);
            } else {
                RedisUtil.hashRedis().put(key, memberId, true);
                num++;
                saveRecord(param, false);
            }
        }
        return num;
    }

    @Override
    public IPage<MemberInteractionResult> pageMemberInteraction(MemberInteractionPageParam param) {
        return memberInteractionMapper.selectInteractionPage(param.page(), Wrappers.<MemberInteraction>lambdaQuery()
                .eq(MemberInteraction::getBusinessId, param.getBusinessId())
                .eq(MemberInteraction::getBusinessType, param.getBusinessType())
                .eq(MemberInteraction::getInteractionType, param.getInteractionType())
                .orderByDesc(MemberInteraction::getInteractionTime)
        );
    }
}
