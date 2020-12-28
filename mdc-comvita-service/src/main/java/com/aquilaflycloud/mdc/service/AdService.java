package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.model.ad.AdInfo;
import com.aquilaflycloud.mdc.param.ad.*;
import com.aquilaflycloud.mdc.result.ad.AdInfoResult;
import com.aquilaflycloud.mdc.result.ad.EventStatisticsResult;
import com.aquilaflycloud.mdc.result.ad.StatisticsResult;
import com.aquilaflycloud.mdc.result.member.MemberEventAnalysisResult;
import com.aquilaflycloud.mdc.result.member.MemberEventSexAndAgeResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * AdService
 *
 * @author star
 * @date 2019-11-18
 */
public interface AdService {
    IPage<AdInfo> pageAd(AdPageParam param);

    AdInfoResult getAd(AdGetParam param);

    void deleteAd(AdGetParam param);

    void add(AdAddParam param);

    void edit(AdEditParam param);

    void toggleState(AdGetParam param);

    StatisticsResult getStatistics(AuthParam param);

    void audit(AdAuditParam param);

    EventStatisticsResult getAdEventStatistics(AdEventStatisticsGetParam param);

    List<MemberEventAnalysisResult> getAdEventAnalysis(AdEventAnalysisGetParam param);

    MemberEventSexAndAgeResult getAdEventDistribution(AdEventDistributionGetParam param);

    List<AdInfoResult> listAd(AdListParam param);

    AdInfo getAdInfo(AdGetParam param);
}
