package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.param.member.MemberEventAnalysisParam;
import com.aquilaflycloud.mdc.param.member.MemberEventParam;
import com.aquilaflycloud.mdc.param.member.MemberEventStatisticsParam;
import com.aquilaflycloud.mdc.result.member.MemberEventAnalysisResult;
import com.aquilaflycloud.mdc.result.member.MemberEventSexAndAgeResult;
import com.aquilaflycloud.mdc.result.member.MemberEventStatisticsResult;

import java.util.List;

/**
 * MemberEventLogService
 *
 * @author star
 * @date 2019-11-19
 */
public interface MemberEventLogService {
    /**
     * 获取业务事件pv
     * @param param 事件入参
     * @return 事件pv
     */
    Long getBusinessNum(MemberEventParam param);

    /**
     * 记录并获取业务事件pv
     * @param param 事件入参
     * @return 事件pv
     */
    Long increaseBusinessNum(MemberEventParam param);

    /**
     * 获取业务事件uv
     * @param param 事件入参
     * @return 事件uv
     */
    Long getBusinessMemberNum(MemberEventParam param);

    /**
     * 记录并获取业务事件uv
     * @param param 事件入参
     * @return 事件uv
     */
    Long increaseBusinessMemberNum(MemberEventParam param);

    /**
     * 获取业务事件次数统计
     * @param param 入参查看参数说明
     * @return 返回每类事件的pv和uv记录数(每类eventType一条记录)
     */
    List<MemberEventStatisticsResult> selectLogStatistics(MemberEventStatisticsParam param);

    /**
     * 获取业务事件次数分析
     * @param param 入参查看参数说明
     * @return 返回每天事件数(根据参数返回pv或uv)
     */
    List<MemberEventAnalysisResult> selectLogAnalysis(MemberEventAnalysisParam param);

    MemberEventSexAndAgeResult selectLogSexAndAge(MemberEventAnalysisParam param);
}

