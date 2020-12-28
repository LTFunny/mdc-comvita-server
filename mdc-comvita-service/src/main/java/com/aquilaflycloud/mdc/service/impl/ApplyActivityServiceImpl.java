package com.aquilaflycloud.mdc.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaMsgService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.constant.MdcConstant;
import com.aquilaflycloud.mdc.enums.apply.ApplyStateEnum;
import com.aquilaflycloud.mdc.enums.common.AuditStateEnum;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageTypeEnum;
import com.aquilaflycloud.mdc.extra.wechat.util.WechatFactoryUtil;
import com.aquilaflycloud.mdc.mapper.ApplyActivityMapper;
import com.aquilaflycloud.mdc.mapper.ApplyMemberRecordMapper;
import com.aquilaflycloud.mdc.mapper.WechatMiniProgramMessageMapper;
import com.aquilaflycloud.mdc.model.apply.ApplyActivity;
import com.aquilaflycloud.mdc.model.apply.ApplyMemberRecord;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.wechat.WechatMiniProgramMessage;
import com.aquilaflycloud.mdc.param.apply.*;
import com.aquilaflycloud.mdc.result.apply.*;
import com.aquilaflycloud.mdc.service.ApplyActivityService;
import com.aquilaflycloud.mdc.service.FolksonomyService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ApplyActivityServiceImpl
 *
 * @author star
 * @date 2020-02-27
 */
@Slf4j
@Service
public class ApplyActivityServiceImpl implements ApplyActivityService {
    @Resource
    private ApplyActivityMapper applyActivityMapper;
    @Resource
    private ApplyMemberRecordMapper applyMemberRecordMapper;
    @Resource
    private WechatMiniProgramMessageMapper wechatMiniProgramMessageMapper;
    @Resource
    private FolksonomyService folksonomyService;

    private ApplyActivity stateHandler(ApplyActivity applyActivity) {
        if (applyActivity == null) {
            throw new ServiceException("报名活动不存在");
        }
        DateTime now = DateTime.now();
        if (applyActivity.getState() != ApplyStateEnum.DISABLE) {
            if (now.isBefore(applyActivity.getStartTime())) {
                applyActivity.setState(ApplyStateEnum.NOTSTART);
            } else if (now.isAfter(applyActivity.getEndTime())) {
                applyActivity.setState(ApplyStateEnum.EXPIRED);
            }
        }
        return applyActivity;
    }

    private Long getApplyNum(Long applyId, Boolean... reload) {
        if (reload.length > 0 && reload[0]) {
            RedisUtil.hashRedis().delete("applyCount", applyId);
        }
        Long num;
        if (RedisUtil.<Long, Long>hashRedis().hasKey("applyCount", applyId)) {
            num = RedisUtil.<Long, Long>hashRedis().get("applyCount", applyId);
        } else {
            num = applyMemberRecordMapper.selectCount(Wrappers.<ApplyMemberRecord>lambdaQuery()
                    .eq(ApplyMemberRecord::getApplyId, applyId)
            ).longValue();
            RedisUtil.<Long, Long>hashRedis().put("applyCount", applyId, num);
        }
        RedisUtil.redis().expire("applyCount", 1, TimeUnit.DAYS);
        return num;
    }

    private Long getApplySuccessNum(Long applyId, Boolean... reload) {
        if (reload.length > 0 && reload[0]) {
            RedisUtil.hashRedis().delete("applySuccessCount", applyId);
        }
        Long successNum;
        if (RedisUtil.<Long, Long>hashRedis().hasKey("applySuccessCount", applyId)) {
            successNum = RedisUtil.<Long, Long>hashRedis().get("applySuccessCount", applyId);
        } else {
            successNum = applyMemberRecordMapper.selectCount(Wrappers.<ApplyMemberRecord>lambdaQuery()
                    .eq(ApplyMemberRecord::getApplyId, applyId)
                    .eq(ApplyMemberRecord::getAuditState, AuditStateEnum.APPROVE)
            ).longValue();
            RedisUtil.<Long, Long>hashRedis().put("applySuccessCount", applyId, successNum);
        }
        RedisUtil.redis().expire("applySuccessCount", 1, TimeUnit.DAYS);
        return successNum;
    }

    @Override
    public IPage<ApplyResult> pageApply(ApplyPageParam param) {
        DateTime now = DateTime.now();
        if (param.getValid() == WhetherEnum.YES) {
            param.setState(null);
        }
        ApplyStateEnum state = param.getState();
        return applyActivityMapper.selectPage(param.page(), new QueryWrapper<ApplyActivity>()
                .orderByDesc("is_top", "case when is_top = 1 then set_top_time else create_time end").lambda()
                .like(StrUtil.isNotBlank(param.getApplyName()), ApplyActivity::getApplyName, param.getApplyName())
                .eq(StrUtil.isNotBlank(param.getAppId()), ApplyActivity::getAppId, param.getAppId())
                .like(param.getCreatorName() != null, ApplyActivity::getCreatorName, param.getCreatorName())
                .ge(param.getCreateTimeStart() != null, ApplyActivity::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, ApplyActivity::getCreateTime, param.getCreateTimeEnd())
                .ge(param.getValidTimeStart() != null, ApplyActivity::getStartTime, param.getValidTimeStart())
                .le(param.getValidTimeEnd() != null, ApplyActivity::getEndTime, param.getValidTimeEnd())
                .and(state == ApplyStateEnum.NORMAL, i -> i
                        .eq(ApplyActivity::getState, ApplyStateEnum.NORMAL)
                        .le(ApplyActivity::getStartTime, now)
                        .ge(ApplyActivity::getEndTime, now))
                .eq(state == ApplyStateEnum.DISABLE, ApplyActivity::getState, ApplyStateEnum.DISABLE)
                .and(state == ApplyStateEnum.NOTSTART, i -> i
                        .eq(ApplyActivity::getState, ApplyStateEnum.NORMAL)
                        .gt(ApplyActivity::getStartTime, now))
                .and(state == ApplyStateEnum.EXPIRED, i -> i
                        .eq(ApplyActivity::getState, ApplyStateEnum.NORMAL)
                        .lt(ApplyActivity::getEndTime, now))
                .and(param.getValid() == WhetherEnum.YES, i -> i
                        .eq(ApplyActivity::getState, ApplyStateEnum.NORMAL)
                        .ge(ApplyActivity::getEndTime, now))
        ).convert(this::stateHandler).convert(apply -> {
            ApplyResult result = new ApplyResult();
            BeanUtil.copyProperties(apply, result);
            result.setApplyNum(getApplyNum(apply.getId()));
            result.setApplySuccessNum(getApplySuccessNum(apply.getId()));
            result.setIntroduction(null);
            return result;
        });
    }

    @Override
    public ApplyDetailResult getApply(ApplyGetParam param) {
        ApplyActivity apply = applyActivityMapper.selectById(param.getId());
        apply = stateHandler(apply);
        ApplyDetailResult result = Convert.convert(ApplyDetailResult.class, apply);
        Integer pendingNum = applyMemberRecordMapper.selectCount(Wrappers.<ApplyMemberRecord>lambdaQuery()
                .eq(ApplyMemberRecord::getApplyId, param.getId())
                .eq(ApplyMemberRecord::getAuditState, AuditStateEnum.PENDING)
        );
        result.setApplyNum(getApplyNum(apply.getId(), true));
        Long successNum = getApplySuccessNum(apply.getId(), true);
        result.setApplySuccessNum(successNum);
        result.setApplyPendingNum(Convert.toLong(pendingNum));
        result.setApplySurplusNum(NumberUtil.sub(apply.getApplyLimit(), successNum).longValue());
        result.setApplyRule(JSONUtil.toBean(apply.getApplyRuleContent(), ApplyRule.class));
        List<FolksonomyInfo> folksonomyInfoList = folksonomyService.getFolksonomyBusinessList(BusinessTypeEnum.APPLY, apply.getId());
        result.setFolksonomyInfoList(folksonomyInfoList);
        return result;
    }

    @Override
    public void addApply(ApplyAddParam param) {
        ApplyActivity apply = new ApplyActivity();
        BeanUtil.copyProperties(param, apply);
        ApplyRule applyRule = param.getApplyRule();
        if (applyRule == null) {
            applyRule = new ApplyRule();
        }
        apply.setApplyRuleContent(JSONUtil.toJsonStr(applyRule));
        apply.setState(ApplyStateEnum.NORMAL);
        if (apply.getIsTop() == WhetherEnum.YES) {
            apply.setSetTopTime(DateTime.now());
        }
        int count = applyActivityMapper.insert(apply);
        if (count <= 0) {
            throw new ServiceException("新增报名活动失败");
        }
        //保存业务功能标签
        folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.APPLY, apply.getId(), param.getFolksonomyIds());
    }

    @Override
    public void editApply(ApplyEditParam param) {
        ApplyActivity apply = applyActivityMapper.selectById(param.getId());
        if (apply.getIsTop() == WhetherEnum.NO && param.getIsTop() == WhetherEnum.YES) {
            apply.setSetTopTime(DateTime.now());
        }
        BeanUtil.copyProperties(param, apply);
        apply.setApplyRuleContent(JSONUtil.toJsonStr(param.getApplyRule()));
        int count = applyActivityMapper.updateById(apply);
        if (count <= 0) {
            throw new ServiceException("编辑报名活动失败");
        }
        //保存业务功能标签
        folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.AD, apply.getId(), param.getFolksonomyIds());
    }

    @Override
    public void toggleTop(ApplyGetParam param) {
        ApplyActivity apply = applyActivityMapper.selectById(param.getId());
        LambdaUpdateWrapper<ApplyActivity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(ApplyActivity::getId, param.getId())
                .set(ApplyActivity::getIsTop, apply.getIsTop() == WhetherEnum.YES ? WhetherEnum.NO : WhetherEnum.YES);
        if (apply.getIsTop() == WhetherEnum.NO) {
            updateWrapper.set(ApplyActivity::getSetTopTime, DateTime.now());
        } else {
            updateWrapper.set(ApplyActivity::getSetTopTime, null);
        }
        int count = applyActivityMapper.update(new ApplyActivity(), updateWrapper);
        if (count <= 0) {
            throw new ServiceException("操作失败");
        }
    }

    @Override
    public void toggleState(ApplyGetParam param) {
        ApplyActivity apply = applyActivityMapper.selectById(param.getId());
        ApplyActivity update = new ApplyActivity();
        update.setId(param.getId());
        update.setState(apply.getState() == ApplyStateEnum.NORMAL ? ApplyStateEnum.DISABLE : ApplyStateEnum.NORMAL);
        int count = applyActivityMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("操作失败");
        }
    }

    @Override
    public StatisticsResult getStatistics() {
        StatisticsResult result = new StatisticsResult();
        Integer total = applyActivityMapper.selectCount(Wrappers.lambdaQuery());
        DateTime now = DateTime.now();
        Integer effectiveTotal = applyActivityMapper.selectCount(Wrappers.<ApplyActivity>lambdaQuery()
                .eq(ApplyActivity::getState, ApplyStateEnum.NORMAL)
                .ge(ApplyActivity::getEndTime, now)
        );
        Integer applyTotal = applyMemberRecordMapper.selectCount(Wrappers.lambdaQuery());
        Integer successTotal = applyMemberRecordMapper.selectCount(Wrappers.<ApplyMemberRecord>lambdaQuery()
                .eq(ApplyMemberRecord::getAuditState, AuditStateEnum.APPROVE)
        );
        result.setTotal(Convert.toLong(total));
        result.setEffectiveTotal(Convert.toLong(effectiveTotal));
        result.setApplyTotal(Convert.toLong(applyTotal));
        result.setApplySuccessTotal(Convert.toLong(successTotal));
        return result;
    }

    @Transactional
    @Override
    public void auditRecord(RecordAuditParam param) {
        ApplyMemberRecord record = applyMemberRecordMapper.selectById(param.getId());
        if (record == null) {
            throw new ServiceException("报名记录不存在");
        }
        ApplyActivity apply = applyActivityMapper.selectById(record.getApplyId());
        RedisUtil.transactionalLock("applyCountChange" + apply.getId());
        if (record.getAuditState() == AuditStateEnum.PENDING) {
            ApplyMemberRecord update = new ApplyMemberRecord();
            update.setId(param.getId());
            update.setAuditState(param.getIsApprove() ? AuditStateEnum.APPROVE : AuditStateEnum.REJECT);
            update.setAuditRemark(param.getAuditRemark());
            int count = applyMemberRecordMapper.updateById(update);
            if (count <= 0) {
                throw new ServiceException("审核失败");
            }
            if (param.getIsApprove()) {
                Long applySuccessNum = getApplySuccessNum(apply.getId());
                if (apply.getApplyLimit() != 0) {
                    if (apply.getApplyLimit() < applySuccessNum + count) {
                        throw new ServiceException("成功报名人数已超上限");
                    } else if (ObjectUtil.equal(apply.getApplyLimit(), applySuccessNum + count)) {
                        //报名人数已满,把剩余需要审核的记录改为不通过
                        update = new ApplyMemberRecord();
                        update.setAuditState(AuditStateEnum.REJECT);
                        update.setAuditRemark(AuditStateEnum.REJECT.getName());
                        applyMemberRecordMapper.update(update, Wrappers.<ApplyMemberRecord>lambdaUpdate()
                                .eq(ApplyMemberRecord::getApplyId, apply.getId())
                                .eq(ApplyMemberRecord::getAuditState, AuditStateEnum.PENDING)
                        );
                    }
                }
                RedisUtil.<Long, Long>hashRedis().put("applySuccessCount", record.getApplyId(), applySuccessNum + count);
            }
            //发送微信订阅消息
            List<MiniMemberInfo> list = new ArrayList<>();
            list.add(new MiniMemberInfo().setAppId(record.getAppId()).setOpenId(record.getOpenId()));
            if (param.getIsApprove()) {
                sendMiniMessage(list, apply.getId(), apply.getApplyName(), "通过", SUCCESS_MSG, DateTime.now().toString(DatePattern.NORM_DATETIME_MINUTE_PATTERN));
            } else {
                sendMiniMessage(list, apply.getId(), apply.getApplyName(), "不通过", FAILED_MSG, DateTime.now().toString(DatePattern.NORM_DATETIME_MINUTE_PATTERN));
            }
        } else {
            throw new ServiceException("该报名记录不需审核");
        }
    }

    @Transactional
    @Override
    public void batchAuditRecord(RecordBatchAuditParam param) {
        if (!param.getIsSurplus() && param.getIds().size() == 0) {
            throw new ServiceException("记录ids不能为空");
        }
        RedisUtil.transactionalLock("applyCountChange" + param.getApplyId());
        List<Long> ids;
        if (param.getIsSurplus()) {
            ids = applyMemberRecordMapper.selectMaps(Wrappers.<ApplyMemberRecord>lambdaQuery()
                    .select(ApplyMemberRecord::getId)
                    .eq(ApplyMemberRecord::getApplyId, param.getApplyId())
                    .eq(ApplyMemberRecord::getAuditState, AuditStateEnum.PENDING)
            ).stream().map(map -> Convert.toLong(map.get("id"))).collect(Collectors.toList());
        } else {
            ids = param.getIds();
        }
        if (ids.size() == 0) {
            throw new ServiceException("无可审批记录");
        }
        ApplyMemberRecord update = new ApplyMemberRecord();
        update.setAuditState(param.getIsApprove() ? AuditStateEnum.APPROVE : AuditStateEnum.REJECT);
        update.setAuditRemark(param.getAuditRemark());
        int count = applyMemberRecordMapper.update(update, Wrappers.<ApplyMemberRecord>lambdaUpdate()
                .in(ApplyMemberRecord::getId, ids)
                .eq(ApplyMemberRecord::getApplyId, param.getApplyId())
                .eq(ApplyMemberRecord::getAuditState, AuditStateEnum.PENDING)
        );
        if (count <= 0) {
            throw new ServiceException("审核失败");
        }
        ApplyActivity apply = applyActivityMapper.selectById(param.getApplyId());
        if (param.getIsApprove()) {
            Long applySuccessNum = getApplySuccessNum(apply.getId());
            if (apply.getApplyLimit() != 0) {
                if (apply.getApplyLimit() < applySuccessNum + count) {
                    throw new ServiceException("成功报名人数已超上限");
                } else if (ObjectUtil.equal(apply.getApplyLimit(), applySuccessNum + count)) {
                    //报名人数已满,把剩余需要审核的记录改为不通过
                    update = new ApplyMemberRecord();
                    update.setAuditState(AuditStateEnum.REJECT);
                    update.setAuditRemark(AuditStateEnum.REJECT.getName());
                    applyMemberRecordMapper.update(update, Wrappers.<ApplyMemberRecord>lambdaUpdate()
                            .eq(ApplyMemberRecord::getApplyId, apply.getId())
                            .eq(ApplyMemberRecord::getAuditState, AuditStateEnum.PENDING)
                    );
                }
            }
            RedisUtil.<Long, Long>hashRedis().put("applySuccessCount", param.getApplyId(), applySuccessNum + count);
        }
        //发送微信订阅消息
        List<ApplyMemberRecord> recordList = applyMemberRecordMapper.selectList(Wrappers.<ApplyMemberRecord>lambdaQuery()
                .in(ApplyMemberRecord::getId, ids)
                .eq(ApplyMemberRecord::getApplyId, param.getApplyId())
                .ne(ApplyMemberRecord::getAuditState, AuditStateEnum.PENDING)
        );
        List<MiniMemberInfo> list = new ArrayList<>();
        for (ApplyMemberRecord record : recordList) {
            list.add(new MiniMemberInfo().setAppId(record.getAppId()).setOpenId(record.getOpenId()));
        }
        if (param.getIsApprove()) {
            sendMiniMessage(list, apply.getId(), apply.getApplyName(), "通过", SUCCESS_MSG, DateTime.now().toString(DatePattern.NORM_DATETIME_MINUTE_PATTERN));
        } else {
            sendMiniMessage(list, apply.getId(), apply.getApplyName(), "不通过", FAILED_MSG, DateTime.now().toString(DatePattern.NORM_DATETIME_MINUTE_PATTERN));
        }
    }

    @Override
    public IPage<ApplyMemberRecord> pageRecord(RecordPageParam param) {
        return applyMemberRecordMapper.selectPage(param.page(), Wrappers.<ApplyMemberRecord>lambdaQuery()
                .eq(param.getApplyId() != null, ApplyMemberRecord::getApplyId, param.getApplyId())
                .like(StrUtil.isNotBlank(param.getNickName()), ApplyMemberRecord::getNickName, param.getNickName())
                .like(StrUtil.isNotBlank(param.getApplyName()), ApplyMemberRecord::getApplyName, param.getApplyName())
                .like(StrUtil.isNotBlank(param.getApplyPhone()), ApplyMemberRecord::getApplyPhone, param.getApplyPhone())
                .eq(param.getAuditState() != null, ApplyMemberRecord::getAuditState, param.getAuditState())
                .ge(param.getCreateTimeStart() != null, ApplyMemberRecord::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, ApplyMemberRecord::getCreateTime, param.getCreateTimeEnd())
        );
    }

    @Override
    public IPage<ApplyResult> pageApplyActivity(ApplyPageParam param) {
        String otherAppId = MdcUtil.getOtherAppId();
        return applyActivityMapper.selectPage(param.page(), new QueryWrapper<ApplyActivity>()
                .orderByDesc("is_top", "case when is_top = 1 then set_top_time else create_time end")
                .lambda()
                .like(StrUtil.isNotBlank(param.getApplyName()), ApplyActivity::getApplyName, param.getApplyName())
                .and(i -> i.eq(StrUtil.isNotBlank(otherAppId), ApplyActivity::getAppId, otherAppId)
                        .or().eq(ApplyActivity::getAppId, MdcConstant.UNIVERSAL_APP_ID)
                )
                .ne(ApplyActivity::getState, ApplyStateEnum.DISABLE)
        ).convert(this::stateHandler).convert(apply -> {
            apply = stateHandler(apply);
            ApplyResult result = new ApplyResult();
            BeanUtil.copyProperties(apply, result);
            result.setApplyNum(getApplyNum(apply.getId()));
            result.setApplySuccessNum(getApplySuccessNum(apply.getId()));
            result.setIntroduction(null);
            return result;
        });
    }

    @Override
    public ApplyActivityDetailResult getApplyActivity(ApplyGetParam param) {
        ApplyActivity apply = applyActivityMapper.selectById(param.getId());
        apply = stateHandler(apply);
        ApplyActivityDetailResult result = Convert.convert(ApplyActivityDetailResult.class, apply);
        result.setApplyNum(getApplyNum(apply.getId()));
        result.setApplySuccessNum(getApplySuccessNum(apply.getId()));
        Long memberId = MdcUtil.getCurrentMemberId();
        if (memberId != null) {
            List<ApplyMemberRecord> recordList = applyMemberRecordMapper.selectList(Wrappers.<ApplyMemberRecord>lambdaQuery()
                    .eq(ApplyMemberRecord::getMemberId, memberId)
                    .eq(ApplyMemberRecord::getApplyId, param.getId())
                    .orderByDesc(ApplyMemberRecord::getCreateTime)
            );
            result.setRecordList(recordList);
        }
        if (result.getApplyNum() > 0) {
            List<String> appliedAvatar = applyMemberRecordMapper.selectList(Wrappers.<ApplyMemberRecord>lambdaQuery()
                    .eq(ApplyMemberRecord::getApplyId, param.getId())
                    .orderByDesc(ApplyMemberRecord::getCreateTime)
                    .last("limit 5")
            ).stream().map(ApplyMemberRecord::getAvatarUrl).collect(Collectors.toList());
            result.setAppliedAvatar(appliedAvatar);
        }
        return result;
    }

    @Override
    public void addApplyRecord(RecordAddParam param) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        ApplyActivity apply = applyActivityMapper.selectById(param.getApplyId());
        apply = stateHandler(apply);
        if (apply.getState() != ApplyStateEnum.NORMAL && apply.getState() != ApplyStateEnum.NOTSTART) {
            throw new ServiceException("该活动" + apply.getState().getName() + "不能报名");
        }
        ApplyRule applyRule = JSONUtil.toBean(apply.getApplyRuleContent(), ApplyRule.class);
        if (!applyRule.getCanApplyAfterStart() && apply.getStartTime().before(DateTime.now())) {
            throw new ServiceException("该活动已开始不能报名");
        }
        if (applyRule.getNeedName() && StrUtil.isBlank(param.getApplyName())) {
            throw new ServiceException("报名人姓名不能为空");
        }
        if (applyRule.getNeedPhone() && StrUtil.isBlank(param.getApplyPhone())) {
            throw new ServiceException("报名电话不能为空");
        }
        if (applyRule.getNeedPhoto()) {
            if (CollUtil.isEmpty(param.getApplyImgList())) {
                throw new ServiceException("报名图片不能为空");
            }
            if (param.getApplyImgList().size() < applyRule.getNeedPhotoCount()) {
                throw new ServiceException("报名图片数量不够");
            }
        }
        ApplyActivity finalApply = apply;
        ApplyMemberRecord applyMemberRecord = RedisUtil.syncLoad("applyRecordAddLock" + finalApply.getId(), () -> {
            if (finalApply.getApplyLimit() > 0) {
                Integer applyCount = applyMemberRecordMapper.selectCount(Wrappers.<ApplyMemberRecord>lambdaQuery()
                        .eq(ApplyMemberRecord::getApplyId, param.getApplyId())
                        .eq(ApplyMemberRecord::getAuditState, AuditStateEnum.APPROVE)
                );
                if (applyCount >= finalApply.getApplyLimit()) {
                    throw new ServiceException("该活动报名人数已满");
                }
            }
            if (!applyRule.getCanRepeat()) {
                Integer appliedCount = applyMemberRecordMapper.selectCount(Wrappers.<ApplyMemberRecord>lambdaQuery()
                        .eq(ApplyMemberRecord::getMemberId, memberInfo.getId())
                        .eq(ApplyMemberRecord::getApplyId, param.getApplyId())
                );
                if (appliedCount > 0) {
                    throw new ServiceException("不能重复报名");
                }
            }
            if (!applyRule.getCanRepeatPhone()) {
                Integer appliedCount = applyMemberRecordMapper.selectCount(Wrappers.<ApplyMemberRecord>lambdaQuery()
                        .eq(ApplyMemberRecord::getApplyPhone, param.getApplyPhone())
                        .eq(ApplyMemberRecord::getApplyId, param.getApplyId())
                );
                if (appliedCount > 0) {
                    throw new ServiceException("该手机不能重复报名");
                }
            }
            ApplyMemberRecord record = new ApplyMemberRecord();
            BeanUtil.copyProperties(param, record);
            record.setId(null);
            record.setApplyId(param.getApplyId());
            record.setApplyImgs(JSONUtil.toJsonStr(param.getApplyImgList()));
            MdcUtil.setMemberInfo(record, memberInfo);
            if (applyRule.getNeedAudit()) {
                record.setAuditState(AuditStateEnum.PENDING);
            } else {
                record.setAuditState(AuditStateEnum.APPROVE);
            }
            int count = applyMemberRecordMapper.insert(record);
            if (count <= 0) {
                throw new ServiceException("报名失败");
            }
            Long applyNum = getApplyNum(param.getApplyId());
            RedisUtil.<Long, Long>hashRedis().put("applyCount", param.getApplyId(), applyNum + 1);
            if (!applyRule.getNeedAudit()) {
                Long applySuccessNum = getApplySuccessNum(param.getApplyId());
                RedisUtil.<Long, Long>hashRedis().put("applySuccessCount", param.getApplyId(), applySuccessNum + 1);
            }
            return record;
        });
        //发送微信订阅消息
        List<MiniMemberInfo> list = new ArrayList<>();
        list.add(new MiniMemberInfo().setAppId(applyMemberRecord.getAppId()).setOpenId(applyMemberRecord.getOpenId()));
        if (applyMemberRecord.getAuditState() == AuditStateEnum.APPROVE) {
            sendMiniMessage(list, apply.getId(), apply.getApplyName(), "通过", SUCCESS_MSG, DateTime.now().toString(DatePattern.NORM_DATETIME_MINUTE_PATTERN));
        }
    }

    @Override
    public IPage<ApplyMemberRecord> pageApplyRecord(ApplyRecordPageParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        return applyMemberRecordMapper.selectPage(param.page(), Wrappers.<ApplyMemberRecord>lambdaQuery()
                .eq(ApplyMemberRecord::getMemberId, memberId)
                .eq(ApplyMemberRecord::getApplyId, param.getApplyId())
                .orderByDesc(ApplyMemberRecord::getCreateTime)
        );
    }

    @Override
    public IPage<ApplyMemberRecordResult> pageApplyMemberRecord(PageParam<ApplyMemberRecord> param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        return applyMemberRecordMapper.selectPage(param.page(), Wrappers.<ApplyMemberRecord>lambdaQuery()
                .eq(ApplyMemberRecord::getMemberId, memberId)
                .groupBy(ApplyMemberRecord::getApplyId)
                .orderByDesc(ApplyMemberRecord::getCreateTime)
        ).convert(record -> {
            ApplyMemberRecordResult result = new ApplyMemberRecordResult();
            BeanUtil.copyProperties(record, result);
            ApplyActivity apply = applyActivityMapper.selectById(record.getApplyId());
            apply = stateHandler(apply);
            apply.setIntroduction(null);
            result.setApplyActivity(apply);
            return result;
        });
    }

    private final static String FAILED_MSG = "很遗憾，您提交的报名信息未能通过审核。";
    private final static String SUCCESS_MSG = "您好，您已报名成功，请准时参加活动。";

    @Data
    @Accessors(chain = true)
    private class MiniMemberInfo {
        private String appId;
        private String openId;
    }

    //发送审核结果通知,小程序订阅消息
    private void sendMiniMessage(List<MiniMemberInfo> miniMemberInfos, Long id, String... contents) {
        MdcUtil.getTtlExecutorService().submit(() -> {
            WechatMiniProgramMessage message = wechatMiniProgramMessageMapper.selectOne(Wrappers.<WechatMiniProgramMessage>lambdaQuery()
                    .eq(WechatMiniProgramMessage::getMessageType, MiniMessageTypeEnum.APPLYRECORDAUDIT)
            );
            if (message != null) {
                Map<String, List<MiniMemberInfo>> miniMap = miniMemberInfos.stream().collect(Collectors.groupingBy(MiniMemberInfo::getAppId));
                for (Map.Entry<String, List<MiniMemberInfo>> entry : miniMap.entrySet()) {
                    //微信小程序报名才发送订阅消息
                    if (StrUtil.startWith(entry.getKey(), "wx")) {
                        WxMaMsgService wxMaMsgService = WechatFactoryUtil.getService(entry.getKey(), "getMsgService", "18");
                        for (MiniMemberInfo miniMemberInfo : entry.getValue()) {
                            WxMaSubscribeMessage subscribeMessage = new WxMaSubscribeMessage();
                            subscribeMessage.setToUser(miniMemberInfo.getOpenId());
                            subscribeMessage.setMiniprogramState(message.getMiniState().name());
                            subscribeMessage.setTemplateId(message.getPriTmplId());
                            subscribeMessage.setPage(id != null ? message.getPagePath() + "?id=" + message.getPagePath() : message.getPagePath());
                            subscribeMessage.setLang(message.getMiniLang().name());
                            String[] paramName = message.getParamName().split(",");
                            for (int i = 0; i < paramName.length; i++) {
                                WxMaSubscribeMessage.Data data = new WxMaSubscribeMessage.Data();
                                String name = paramName[i];
                                data.setName(name);
                                data.setValue(contents[i]);
                                subscribeMessage.addData(data);
                            }
                            try {
                                wxMaMsgService.sendSubscribeMsg(subscribeMessage);
                            } catch (WxErrorException e) {
                                log.error("发送小程序订阅消息失败", e);
                            }
                        }
                    }
                }
            }
        });
    }
}
