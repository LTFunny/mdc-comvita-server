package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.alipay.AlipayMemberDailyDataInfo;
import com.aquilaflycloud.mdc.param.member.MemberDailyDataListParam;
import com.aquilaflycloud.mdc.param.member.MemberGrowParam;
import com.aquilaflycloud.mdc.param.member.MemberVisitTimesParam;
import com.aquilaflycloud.mdc.result.member.MemberGrowResult;
import com.aquilaflycloud.mdc.result.member.MemberVisitTimesResult;

import java.util.List;

/**
 * AlipayMemberDailyDataInfoService
 *
 * @author Zengqingjie
 * @date 2020-05-26
 */

public interface MemberDailyDataInfoService {
    List<AlipayMemberDailyDataInfo> listInfos(MemberDailyDataListParam param);

    int handleInfos(AlipayMemberDailyDataInfo info, String type);

    MemberGrowResult memberGrow(MemberGrowParam param);

    MemberVisitTimesResult memberVisitTimes(MemberVisitTimesParam param);
}
