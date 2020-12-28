package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.recommendation.Recommendation;
import com.aquilaflycloud.mdc.param.recommendation.*;
import com.aquilaflycloud.mdc.result.member.MemberEventAnalysisResult;
import com.aquilaflycloud.mdc.result.recommendation.EventStatisticsResult;
import com.aquilaflycloud.mdc.result.recommendation.RecommendationApiResult;
import com.aquilaflycloud.mdc.result.recommendation.RecommendationResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * RecommendationService
 *
 * @author star
 * @date 2020-03-27
 */
public interface RecommendationService {

    IPage<Recommendation> pageRecommendation(RecommendationPageParam param);

    void addRecommendation(RecommendationAddParam param);

    void editRecommendation(RecommendationEditParam param);

    void toggleState(RecommendationGetParam param);

    void toggleIsTop(RecommendationGetParam param);

    void releaseRecommendation(RecommendationGetParam param);

    RecommendationResult getRecommendation(RecommendationGetParam param);

    List<MemberEventAnalysisResult> getEventAnalysis(EventAnalysisGetParam param);

    EventStatisticsResult getEventStatistics(EventStatisticsParam param);

    IPage<Recommendation> page(PageParam param);

    RecommendationApiResult get(RecommendationGetParam param);
}
