package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.enums.member.IdTypeEnum;
import com.aquilaflycloud.mdc.enums.member.SexEnum;
import com.aquilaflycloud.mdc.mapper.MemberHealthRecordMapper;
import com.aquilaflycloud.mdc.mapper.MemberInfoMapper;
import com.aquilaflycloud.mdc.model.member.MemberHealthRecord;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.param.member.HealthAddParam;
import com.aquilaflycloud.mdc.param.member.HealthQuickAddParam;
import com.aquilaflycloud.mdc.service.MemberHealthService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * MemberHealthServiceImpl
 *
 * @author star
 * @date 2020-04-19
 */
@Service
public class MemberHealthServiceImpl implements MemberHealthService {
    @Resource
    private MemberHealthRecordMapper memberHealthRecordMapper;
    @Resource
    private MemberInfoMapper memberInfoMapper;

    @Override
    public MemberHealthRecord getHealth() {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        return memberHealthRecordMapper.selectOne(Wrappers.<MemberHealthRecord>lambdaQuery()
                .eq(MemberHealthRecord::getMemberId, memberId)
                .isNotNull(MemberHealthRecord::getHealthCode)
                .orderByDesc(MemberHealthRecord::getCreateTime)
                .last("limit 1")
        );
    }

    private String getSceneName(String scene) {
        String sceneName = null;
        switch (StrUtil.nullToEmpty(scene)) {
            case "healthCode": {
                sceneName = "正佳广场";
                break;
            }
            case "haiyangguan": {
                sceneName = "海洋馆";
                break;
            }
            case "bowuguan": {
                sceneName = "博物馆";
                break;
            }
            case "yulinguan": {
                sceneName = "雨林馆";
                break;
            }
            default:
        }
        return sceneName;
    }

    @Override
    public void addHealth(HealthAddParam param) {
        if (param.getIdType() == IdTypeEnum.IDCARD && !IdcardUtil.isValidCard(param.getIdCard())) {
            throw new ServiceException("身份证有误");
        }
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        RedisUtil.syncLoad("addHealthLock" + memberInfo.getId(), () -> {
            MemberHealthRecord record = getHealth();
            String healthCode;
            if (record != null) {
                healthCode = record.getHealthCode();
            } else {
                String time = Convert.toStr(DateTime.now().getTime());
                String random = RandomUtil.randomNumbers(4);
                String memberIdStr = Convert.toStr(memberInfo.getId());
                healthCode = StrUtil.subSuf(time, time.length() - 4) + random + StrUtil.subSuf(memberIdStr, memberIdStr.length() - 4);
            }
            record = BeanUtil.copyProperties(param, MemberHealthRecord.class);
            MdcUtil.setMemberInfo(record, memberInfo);
            record.setHealthCode(healthCode);
            record.setSceneName(getSceneName(param.getSceneStr()));
            int count = memberHealthRecordMapper.insert(record);
            if (count <= 0) {
                throw new ServiceException("新增防疫信息失败");
            }
            if (param.getIdType() == IdTypeEnum.IDCARD) {
                try {
                    String idCard = param.getIdCard();
                    String birth = IdcardUtil.getBirth(idCard);
                    int gender = IdcardUtil.getGenderByIdCard(idCard);
                    MemberInfo update = new MemberInfo();
                    update.setIdCard(idCard);
                    update.setRealName(param.getRealName());
                    update.setBirthday(DateUtil.parse(birth));
                    update.setSex(gender == 0 ? SexEnum.FEMALE : SexEnum.MALE);
                    memberInfoMapper.update(update, Wrappers.<MemberInfo>lambdaUpdate()
                            .eq(MemberInfo::getId, memberInfo.getId())
                    );
                } catch (Exception ignored) {
                }
            }
            return null;
        });
    }

    @Override
    public void quickAddHealth(HealthQuickAddParam param) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        MemberHealthRecord record = new MemberHealthRecord();
        MemberHealthRecord lastRecord = getHealth();
        if (lastRecord == null || StrUtil.isBlank(lastRecord.getHealthCode())) {
            return;
        }
        BeanUtil.copyProperties(lastRecord, record, CopyOptions.create().ignoreNullValue().setIgnoreProperties(MdcUtil.getIgnoreNames()));
        if (StrUtil.isBlank(param.getSceneStr())) {
            record.setSceneStr(StrUtil.emptyToNull(param.getSceneStr()));
            record.setSceneName(getSceneName(param.getSceneStr()));
        }
        MdcUtil.setMemberInfo(record, memberInfo);
        int count = memberHealthRecordMapper.insert(record);
        if (count <= 0) {
            throw new ServiceException("快捷新增防疫信息失败");
        }
    }
}
