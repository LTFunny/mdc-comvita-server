package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import com.aquilaflycloud.mdc.mapper.MemberSignRecordMapper;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.member.MemberSignRecord;
import com.aquilaflycloud.mdc.param.member.MemberSignAnalysisParam;
import com.aquilaflycloud.mdc.param.member.MemberSignContinueAnalysisParam;
import com.aquilaflycloud.mdc.param.member.MemberSignPageParam;
import com.aquilaflycloud.mdc.result.member.*;
import com.aquilaflycloud.mdc.service.MemberRewardService;
import com.aquilaflycloud.mdc.service.MemberSignService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MemberSignServiceImpl
 *
 * @author star
 * @date 2020-01-02
 */
@Service
public class MemberSignServiceImpl implements MemberSignService {
    @Resource
    private MemberSignRecordMapper memberSignRecordMapper;
    @Resource
    private MemberRewardService memberRewardService;

    private List<MemberSignContinueResult> getContinueSignCountResult(String appId, Long memberId, Boolean... includeYesterday) {
        MemberSignRecord record = new MemberSignRecord();
        record.setAppId(appId);
        record.setMemberId(memberId);
        record.setTenantId(MdcUtil.getCurrentTenantId());
        record.setSubTenantId(MdcUtil.getCurrentSubTenantId());
        return memberSignRecordMapper.selectContinueSignCount(record, Wrappers.query()
                .having(includeYesterday.length == 0 || BooleanUtil.andOfWrap(includeYesterday),
                        "max_date = {0}", DateTime.now().toDateStr())
                .having(includeYesterday.length > 0 && BooleanUtil.andOfWrap(includeYesterday),
                        "max_date = {0} or max_date = {1}", DateUtil.date().toDateStr(), DateUtil.yesterday().toDateStr())
                .groupBy("group_id")
                .orderByDesc("max_date")
        );
    }

    private Integer getContinueSignCount(String appId, Long memberId) {
        Integer continueSign = 0;
        List<MemberSignContinueResult> continueResultList = getContinueSignCountResult(appId, memberId, true);
        if (continueResultList.size() > 0) {
            continueSign = continueResultList.get(0).getContinueCount();
        }
        return continueSign;
    }

    @Override
    public IPage<MemberSignInfoResult> pageMemberRecord(MemberSignPageParam param) {
        int count = memberSignRecordMapper.normalSelectCount(Wrappers.emptyWrapper());
        return memberSignRecordMapper.selectPageByMemberParam(param.page(), count, Wrappers.<MemberSignRecord>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getPhoneNumber()), MemberSignRecord::getPhoneNumber, param.getPhoneNumber())
                .like(StrUtil.isNotBlank(param.getNickName()), MemberSignRecord::getNickName, param.getNickName())
                .eq(StrUtil.isNotBlank(param.getAppId()), MemberSignRecord::getAppId, param.getAppId())
                .ge(param.getCreateTimeStart() != null, MemberSignRecord::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, MemberSignRecord::getCreateTime, param.getCreateTimeEnd())
                .eq(param.getMemberId() != null, MemberSignRecord::getMemberId, param.getMemberId())
                .groupBy(MemberSignRecord::getMemberId)
                .orderByDesc(MemberSignRecord::getCreateTime)
        ).convert(record -> {
            Integer continueSign = getContinueSignCount(null, record.getMemberId());
            MemberSignInfoResult result = new MemberSignInfoResult();
            BeanUtil.copyProperties(record, result);
            result.setContinueSign(continueSign);
            result.setRewardList(JSONUtil.toList(JSONUtil.parseArray(result.getRewardValueContent()), MemberRewardResult.class));
            return result;
        });
    }

    @Override
    public IPage<MemberSignResult> pageRecord(MemberSignPageParam param) {
        return memberSignRecordMapper.selectPage(param.page(), Wrappers.<MemberSignRecord>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getPhoneNumber()), MemberSignRecord::getPhoneNumber, param.getPhoneNumber())
                .like(StrUtil.isNotBlank(param.getNickName()), MemberSignRecord::getNickName, param.getNickName())
                .eq(StrUtil.isNotBlank(param.getAppId()), MemberSignRecord::getAppId, param.getAppId())
                .ge(param.getCreateTimeStart() != null, MemberSignRecord::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, MemberSignRecord::getCreateTime, param.getCreateTimeEnd())
                .eq(param.getMemberId() != null, MemberSignRecord::getMemberId, param.getMemberId())
                .orderByDesc(MemberSignRecord::getCreateTime)
        ).convert(record -> {
            MemberSignResult result = new MemberSignResult();
            BeanUtil.copyProperties(record, result);
            result.setRewardList(JSONUtil.toList(JSONUtil.parseArray(result.getRewardValueContent()), MemberRewardResult.class));
            return result;
        });
    }

    @Override
    public List<MemberSignAnalysisResult> getSignCountAnalysis(MemberSignAnalysisParam param) {
        Integer total = memberSignRecordMapper.selectCount(Wrappers.<MemberSignRecord>query()
                .select("distinct member_id")
                .lambda()
                .eq(StrUtil.isNotBlank(param.getAppId()), MemberSignRecord::getAppId, param.getAppId())
        );
        Map<Date, MemberSignAnalysisResult> analysis = memberSignRecordMapper.selectMaps(new QueryWrapper<MemberSignRecord>()
                .select("date_format(create_time,'%Y-%m-%d') create_date, count(1) sign_count")
                .groupBy("create_date")
                .lambda()
                .eq(StrUtil.isNotBlank(param.getAppId()), MemberSignRecord::getAppId, param.getAppId())
                .ge(MemberSignRecord::getCreateTime, param.getCreateTimeStart())
                .le(MemberSignRecord::getCreateTime, param.getCreateTimeEnd())
        ).stream().map((map) -> {
            MemberSignAnalysisResult result = new MemberSignAnalysisResult();
            BeanUtil.fillBeanWithMap(map, result, true, true);
            result.setSignRate(total == 0 ? "0.00%" : NumberUtil.formatPercent(NumberUtil.div(result.getSignCount(), total).doubleValue(), 2));
            return result;
        }).collect(Collectors.toMap(MemberSignAnalysisResult::getCreateDate, result -> result));
        return DateUtil.rangeToList(param.getCreateTimeStart(), param.getCreateTimeEnd(), DateField.DAY_OF_YEAR).stream().map(dateTime -> {
            MemberSignAnalysisResult result = analysis.get(dateTime);
            if (result == null) {
                result = new MemberSignAnalysisResult();
                result.setCreateDate(dateTime);
                result.setSignCount(0L);
                result.setSignRate("0%");
            }
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MemberSignContinueAnalysisResult> getContinueSignCountAnalysis(MemberSignContinueAnalysisParam param) {
        Integer total = memberSignRecordMapper.selectCount(Wrappers.<MemberSignRecord>query()
                .select("distinct member_id")
                .lambda()
                .eq(StrUtil.isNotBlank(param.getAppId()), MemberSignRecord::getAppId, param.getAppId())
        );
        List<MemberSignContinueResult> continueResultList = getContinueSignCountResult(param.getAppId(), null);
        return param.getRangList().stream().map(rang -> {
            int start = NumberUtil.max(rang.getStart(), 1);
            int end = NumberUtil.max(rang.getEnd(), start);
            Long count = continueResultList.stream().filter(result -> result.getContinueCount() > start && result.getContinueCount() < end).count();
            MemberSignContinueAnalysisResult result = new MemberSignContinueAnalysisResult();
            result.setRangStr(rang.getStart() + "-" + rang.getEnd());
            result.setSignCount(count);
            String signRate = "0";
            if (total > 0) {
                signRate = NumberUtil.formatPercent(NumberUtil.div(count, total).doubleValue(), 2);
            }
            result.setSignRate(signRate);
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public MemberSignDescInfoResult getSignInfo() {
        String appId = MdcUtil.getOtherAppId();
        MemberSignDescInfoResult result = new MemberSignDescInfoResult();
        MemberInfo memberInfo = MdcUtil.getCurrentMember();
        if (memberInfo != null) {
            Long memberId = memberInfo.getId();
            appId = MdcUtil.getMemberAppId(memberInfo);
            List<String> dateStr = memberSignRecordMapper.selectList(Wrappers.<MemberSignRecord>lambdaQuery()
                    .eq(MemberSignRecord::getMemberId, memberId)
                    .orderByDesc(MemberSignRecord::getCreateTime)
            ).stream().map(record -> new DateTime(record.getCreateTime()).toDateStr()).collect(Collectors.toList());
            result.setSignDateStrList(dateStr);
            result.setTotalSign(dateStr.size());
            if (dateStr.size() > 0) {
                String lastSign = dateStr.get(0);
                if (DateUtil.parse(lastSign).isBefore(DateUtil.beginOfDay(DateUtil.yesterday()))) {
                    result.setContinueSign(0);
                } else {
                    result.setContinueSign(getContinueSignCount(null, memberId));
                }
            }
        }
        result.setRewardRule(memberRewardService.getRewardSignList(appId));
        return result;
    }

    @Transactional
    @Override
    public MemberSignAddResult addSignInfo() {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        Long memberId = memberInfo.getId();
        Integer continueCount = getContinueSignCount(null, memberId);
        RedisUtil.transactionalLock("memberSignLock" + memberId);
        MemberSignAddResult result = new MemberSignAddResult();
        Integer signCount = memberSignRecordMapper.selectCount(Wrappers.<MemberSignRecord>lambdaQuery()
                .eq(MemberSignRecord::getMemberId, memberId)
                .ge(MemberSignRecord::getCreateTime, DateUtil.beginOfDay(DateTime.now()))
        );
        if (signCount > 0) {
            throw new ServiceException("重复签到");
        }
        //验证连续签到数
        int newContinueCount = 1;
        if (continueCount > 0) {
            Integer yesterdaySignCount = memberSignRecordMapper.selectCount(Wrappers.<MemberSignRecord>lambdaQuery()
                    .eq(MemberSignRecord::getMemberId, memberId)
                    .ge(MemberSignRecord::getCreateTime, DateUtil.beginOfDay(DateUtil.yesterday()))
            );
            if (yesterdaySignCount > 0) {
                newContinueCount += continueCount;
            }
        }
        Map<RewardTypeEnum, MemberRewardResult> rewardResult = memberRewardService.addSignRewardRecord(memberInfo, newContinueCount);
        if (CollUtil.isEmpty(rewardResult)) {
            throw new ServiceException("签到未启用");
        }
        MemberSignRecord record = new MemberSignRecord();
        MdcUtil.setMemberInfo(record, memberInfo);
        List<MemberRewardResult> list = CollUtil.newArrayList(rewardResult.values());
        record.setRewardValueContent(JSONUtil.toJsonStr(list));
        int count = memberSignRecordMapper.insert(record);
        if (count <= 0) {
            throw new ServiceException("签到失败");
        }
        result.setRewardResultList(list);
        return result;
    }
}
