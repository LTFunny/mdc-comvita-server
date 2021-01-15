package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.common.CreateSourceEnum;
import com.aquilaflycloud.mdc.enums.common.StateEnum;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.coupon.VerificateStateEnum;
import com.aquilaflycloud.mdc.enums.folksonomy.FolksonomyTypeEnum;
import com.aquilaflycloud.mdc.enums.mission.MissionTypeEnum;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.coupon.CouponMemberRel;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyMemberRel;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.member.MemberSignRecord;
import com.aquilaflycloud.mdc.model.mission.MissionInfo;
import com.aquilaflycloud.mdc.model.mission.MissionMemberRecord;
import com.aquilaflycloud.mdc.param.mission.MissionAddParam;
import com.aquilaflycloud.mdc.param.mission.MissionEditParam;
import com.aquilaflycloud.mdc.param.mission.MissionGetParam;
import com.aquilaflycloud.mdc.param.mission.MissionRuleParam;
import com.aquilaflycloud.mdc.result.mission.MissionInfoResult;
import com.aquilaflycloud.mdc.result.mission.MissionRecordResult;
import com.aquilaflycloud.mdc.result.mission.MissionRuleResult;
import com.aquilaflycloud.mdc.service.MemberRewardService;
import com.aquilaflycloud.mdc.service.MissionService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.aquilaflycloud.util.SpringUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * MissionServiceImpl
 *
 * @author star
 * @date 2020-05-08
 */
@Slf4j
@Service
public class MissionServiceImpl implements MissionService {
    @Resource
    private MissionInfoMapper missionInfoMapper;
    @Resource
    private MissionMemberRecordMapper missionMemberRecordMapper;
    @Resource
    private MemberInfoMapper memberInfoMapper;
    @Resource
    private FolksonomyMemberRelMapper folksonomyMemberRelMapper;
    @Resource
    private MemberSignRecordMapper memberSignRecordMapper;
    @Resource
    private CouponMemberRelMapper couponMemberRelMapper;
    @Resource
    private MemberRewardService memberRewardService;

    @Override
    public List<MissionInfo> listMission() {
        return missionInfoMapper.selectList(Wrappers.lambdaQuery());
    }

    @Override
    public MissionInfoResult getMission(MissionGetParam param) {
        MissionInfo missionInfo = missionInfoMapper.selectById(param.getId());
        MissionInfoResult result = BeanUtil.copyProperties(missionInfo, MissionInfoResult.class);
        result.setMissionRuleList(JSONUtil.toList(JSONUtil.parseArray(missionInfo.getMissionRuleContent()), MissionRuleResult.class));
        return result;
    }

    @Override
    public void addMission(MissionAddParam param) {
        RedisUtil.syncLoad("addMission", () -> {
            MissionInfo missionInfo = missionInfoMapper.selectOne(Wrappers.<MissionInfo>lambdaQuery()
                    .eq(MissionInfo::getMissionType, param.getMissionType())
            );
            if (missionInfo != null) {
                throw new ServiceException("该任务已存在");
            }
            missionInfo = new MissionInfo();
            BeanUtil.copyProperties(param, missionInfo);
            missionInfo.setMissionName(param.getMissionType().getName());
            missionInfo.setMissionRuleContent(JSONUtil.toJsonStr(param.getMissionRuleList()));
            int count = missionInfoMapper.insert(missionInfo);
            if (count <= 0) {
                throw new ServiceException("新增任务失败");
            }
            return null;
        });
        Long tenantId = MdcUtil.getCurrentTenantId();
        RedisUtil.redis().delete("missionList" + tenantId);
        RedisUtil.valueRedis().set("hasMissionList" + tenantId, true);
    }

    @Transactional
    @Override
    public void editMission(MissionEditParam param) {
        MissionInfo missionInfo = missionInfoMapper.selectById(param.getId());
        if (missionInfo == null) {
            throw new ServiceException("任务不存在");
        }
        MissionInfo update = new MissionInfo();
        BeanUtil.copyProperties(param, update);
        if (CollUtil.isNotEmpty(param.getMissionRuleList()) && !StrUtil.equals(missionInfo.getMissionRuleContent(), JSONUtil.toJsonStr(param.getMissionRuleList()))) {
            update.setMissionRuleContent(JSONUtil.toJsonStr(param.getMissionRuleList()));
            for (MissionRuleParam missionRuleParam : param.getMissionRuleList()) {
                MissionMemberRecord recordUpdate = new MissionMemberRecord();
                recordUpdate.setMissionContent(JSONUtil.toJsonStr(missionRuleParam));
                missionMemberRecordMapper.update(recordUpdate, Wrappers.<MissionMemberRecord>lambdaUpdate()
                        .eq(MissionMemberRecord::getMissionId, param.getId())
                        .eq(MissionMemberRecord::getMissionCondition, missionRuleParam.getCondition())
                        .eq(MissionMemberRecord::getFinishState, WhetherEnum.NO)
                );
            }

        }
        int count = missionInfoMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("编辑任务失败");
        }
        Long tenantId = MdcUtil.getCurrentTenantId();
        RedisUtil.redis().delete("missionList" + tenantId);
        RedisUtil.valueRedis().set("hasMissionList" + tenantId, true);
    }

    @Override
    public void toggleMission(MissionGetParam param) {
        MissionInfo missionInfo = missionInfoMapper.selectById(param.getId());
        if (missionInfo == null) {
            throw new ServiceException("任务不存在");
        }
        MissionInfo update = new MissionInfo();
        update.setId(missionInfo.getId());
        update.setState(missionInfo.getState() == StateEnum.NORMAL ? StateEnum.DISABLE : StateEnum.NORMAL);
        int count = missionInfoMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("操作失败");
        }
        int normalCount = missionInfoMapper.selectCount(Wrappers.<MissionInfo>lambdaQuery()
                .eq(MissionInfo::getState, StateEnum.NORMAL)
        );
        Long tenantId = MdcUtil.getCurrentTenantId();
        if (normalCount > 0) {
            RedisUtil.valueRedis().set("hasMissionList" + tenantId, true);
        } else {
            RedisUtil.valueRedis().set("hasMissionList" + tenantId, false);
        }
    }

    private List<MissionInfo> listMissionCache() {
        Long tenantId = MdcUtil.getCurrentTenantId();
        List<MissionInfo> missionList = RedisUtil.<MissionInfo>listRedis().range("missionList" + tenantId, 0, -1);
        Boolean hasMissionList = RedisUtil.<Boolean>valueRedis().get("hasMissionList" + tenantId);
        if (CollUtil.isEmpty(missionList) && BooleanUtil.isTrue(hasMissionList)) {
            missionList = missionInfoMapper.selectList(Wrappers.<MissionInfo>lambdaQuery()
                    .eq(MissionInfo::getState, StateEnum.NORMAL)
            );
            RedisUtil.<MissionInfo>listRedis().leftPushAll("missionList" + tenantId, missionList);
            RedisUtil.redis().expire("missionList" + tenantId, 7, TimeUnit.DAYS);
        }
        return missionList;
    }

    @Override
    public IPage<MissionRecordResult> pageRecord(PageParam<MissionMemberRecord> param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        return missionMemberRecordMapper.selectPage(param.page(), Wrappers.<MissionMemberRecord>lambdaQuery()
                .eq(MissionMemberRecord::getMemberId, memberId)
                .orderByAsc(MissionMemberRecord::getFinishState, MissionMemberRecord::getMissionType)
                .orderByDesc(MissionMemberRecord::getCreateTime)
        ).convert(mission -> {
            MissionRecordResult result = BeanUtil.copyProperties(mission, MissionRecordResult.class);
            result.setMissionRule(JSONUtil.toBean(result.getMissionContent(), MissionRuleResult.class));
            return result;
        });
    }

    private void addRecord(MemberInfo memberInfo, MissionInfo missionInfo, MissionRuleResult result) {
        MissionMemberRecord record = new MissionMemberRecord();
        BeanUtil.copyProperties(missionInfo, record, CopyOptions.create().setIgnoreProperties(MdcUtil.getIgnoreNames()));
        MdcUtil.setMemberInfo(record, memberInfo);
        record.setMissionId(missionInfo.getId());
        record.setFinishState(WhetherEnum.NO);
        record.setMissionCondition(result.getCondition());
        record.setMissionContent(JSONUtil.toJsonStr(result));
        missionMemberRecordMapper.insert(record);
    }

    @Override
    public void acceptMission(MemberInfo memberInfo) {
        Boolean canAccept = RedisUtil.hashRedis().putIfAbsent("canAcceptMission", memberInfo.getId(), memberInfo.getId());
        if (canAccept) {
            MdcUtil.getTtlExecutorService().submit(() -> {
                try {
                    List<MissionInfo> missionList = listMissionCache();
                    for (MissionInfo missionInfo : missionList) {
                        List<MissionRuleResult> resultList = JSONUtil.toList(JSONUtil.parseArray(missionInfo.getMissionRuleContent()), MissionRuleResult.class);
                        if (CollUtil.isNotEmpty(resultList)) {
                            for (MissionRuleResult result : resultList) {
                                MissionMemberRecord record = missionMemberRecordMapper.selectOne(Wrappers.<MissionMemberRecord>lambdaQuery()
                                        .eq(MissionMemberRecord::getMemberId, memberInfo.getId())
                                        .eq(MissionMemberRecord::getMissionId, missionInfo.getId())
                                        .eq(MissionMemberRecord::getMissionCondition, result.getCondition())
                                );
                                if (record == null) {
                                    addRecord(memberInfo, missionInfo, result);
                                }
                            }
                        }
                    }
                    if (CollUtil.isNotEmpty(missionList)) {
                        SpringUtil.getBean(MissionService.class).checkMission(memberInfo.getId(), null);
                    }
                } catch (Exception e) {
                    log.error("会员接受任务失败", e);
                } finally {
                    RedisUtil.hashRedis().delete("canAcceptMission", memberInfo.getId());
                    RedisUtil.redis().expire("canAcceptMission", 30, TimeUnit.DAYS);
                }
            });
        }
    }

    private void finishMission(MemberInfo memberInfo, List<MissionMemberRecord> recordList, Integer condition) {
        for (MissionMemberRecord record : recordList) {
            MissionRuleResult result = JSONUtil.toBean(record.getMissionContent(), MissionRuleResult.class);
            if (condition >= result.getCondition()) {
                MissionMemberRecord update = new MissionMemberRecord();
                update.setId(record.getId());
                update.setFinishState(WhetherEnum.YES);
                int count = missionMemberRecordMapper.updateById(update);
                if (count > 0) {
                    memberRewardService.addMissionRewardRecord(memberInfo, result.getRewardType(), result.getRewardValue(), record.getMissionId());
                }
            }
        }
    }

    @Transactional
    @Override
    public void checkMission(Long memberId, MissionTypeEnum missionType) {
        if (memberId == null) {
            return;
        }
        MemberInfo memberInfo = memberInfoMapper.selectById(memberId);
        if (memberInfo == null) {
            return;
        }
        Set<MissionTypeEnum> missionTypeSet = new HashSet<>();
        if (missionType == null) {
            missionTypeSet.addAll(CollUtil.toList(MissionTypeEnum.values()));
        } else {
            missionTypeSet.add(missionType);
        }
        for (MissionTypeEnum type : missionTypeSet) {
            List<MissionMemberRecord> recordList = missionMemberRecordMapper.selectList(Wrappers.<MissionMemberRecord>lambdaQuery()
                    .eq(MissionMemberRecord::getMemberId, memberId)
                    .eq(MissionMemberRecord::getMissionType, type)
                    .eq(MissionMemberRecord::getFinishState, WhetherEnum.NO)
            );
            if (CollUtil.isNotEmpty(recordList)) {
                switch (type) {
                    case COMPLETE: {
                        if (memberInfo.getSex() != null && memberInfo.getBirthday() != null
                                && (StrUtil.isNotBlank(memberInfo.getNickName()) || StrUtil.isNotBlank(memberInfo.getRealName()))) {
                            finishMission(memberInfo, recordList, 1);
                        }
                        break;
                    }
                    case FOLKSONOMY: {
                        int count = folksonomyMemberRelMapper.selectCount(Wrappers.<FolksonomyMemberRel>lambdaQuery()
                                .eq(FolksonomyMemberRel::getMemberId, memberId)
                                .eq(FolksonomyMemberRel::getType, FolksonomyTypeEnum.BUSINESS)
                        );
                        finishMission(memberInfo, recordList, count);
                        break;
                    }
                    case BINDINGPHONE: {
                        if (StrUtil.isNotBlank(memberInfo.getPhoneNumber())) {
                            finishMission(memberInfo, recordList, 1);
                        }
                        break;
                    }
                    case TOTALSIGN: {
                        int count = memberSignRecordMapper.selectCount(Wrappers.<MemberSignRecord>lambdaQuery()
                                .eq(MemberSignRecord::getMemberId, memberId)
                        );
                        finishMission(memberInfo, recordList, count);
                        break;
                    }
                    case RECEIVECOUPON: {
                        int count = couponMemberRelMapper.selectCount(Wrappers.<CouponMemberRel>lambdaQuery()
                                .eq(CouponMemberRel::getMemberId, memberId)
                                .eq(CouponMemberRel::getCreateSource, CreateSourceEnum.NORMAL)
                        );
                        finishMission(memberInfo, recordList, count);
                        break;
                    }
                    case USECOUPON: {
                        int count = couponMemberRelMapper.selectCount(Wrappers.<CouponMemberRel>lambdaQuery()
                                .eq(CouponMemberRel::getMemberId, memberId)
                                .eq(CouponMemberRel::getCreateSource, CreateSourceEnum.NORMAL)
                                .eq(CouponMemberRel::getVerificateState, VerificateStateEnum.CONSUMED)
                        );
                        finishMission(memberInfo, recordList, count);
                        break;
                    }
                    default:
                }
            }
        }
    }
}
