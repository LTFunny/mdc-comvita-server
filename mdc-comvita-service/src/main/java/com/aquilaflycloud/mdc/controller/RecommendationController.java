package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.recommendation.Recommendation;
import com.aquilaflycloud.mdc.param.recommendation.*;
import com.aquilaflycloud.mdc.result.member.MemberEventAnalysisResult;
import com.aquilaflycloud.mdc.result.recommendation.EventStatisticsResult;
import com.aquilaflycloud.mdc.result.recommendation.RecommendationResult;
import com.aquilaflycloud.mdc.service.RecommendationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * RecommendationController
 *
 * @author star
 * @date 2020-03-28
 */
@RestController
@Api(tags = "最新推荐接口")
public class RecommendationController {

    @Resource
    private RecommendationService recommendationService;

    @ApiOperation(value = "查询最新推荐列表", notes = "查询最新推荐列表")
    @PreAuthorize("hasAuthority('mdc:recommendation:list')")
    @ApiMapping(value = "backend.comvita.recommendation.info.page", method = RequestMethod.POST, permission = true)
    public IPage<Recommendation> pageRecommendation(RecommendationPageParam param) {
        return recommendationService.pageRecommendation(param);
    }

    @ApiOperation(value = "新增最新推荐", notes = "新增最新推荐")
    @PreAuthorize("hasAuthority('mdc:recommendation:add')")
    @ApiMapping(value = "backend.comvita.recommendation.info.add", method = RequestMethod.POST, permission = true)
    public void addRecommendation(RecommendationAddParam param) {
        recommendationService.addRecommendation(param);
    }

    @ApiOperation(value = "编辑最新推荐", notes = "编辑最新推荐")
    @PreAuthorize("hasAuthority('mdc:recommendation:edit')")
    @ApiMapping(value = "backend.comvita.recommendation.info.edit", method = RequestMethod.POST, permission = true)
    public void editRecommendation(RecommendationEditParam param) {
        recommendationService.editRecommendation(param);
    }

    @ApiOperation(value = "停用/启用最新推荐", notes = "停用/启用最新推荐")
    @PreAuthorize("hasAuthority('mdc:recommendation:edit')")
    @ApiMapping(value = "backend.comvita.recommendation.state.toggle", method = RequestMethod.POST, permission = true)
    public void toggleState(RecommendationGetParam param) {
        recommendationService.toggleState(param);
    }

    @ApiOperation(value = "取消/置顶最新推荐", notes = "取消/置顶最新推荐")
    @PreAuthorize("hasAuthority('mdc:recommendation:edit')")
    @ApiMapping(value = "backend.comvita.recommendation.info.toggle", method = RequestMethod.POST, permission = true)
    public void toggleIsTop(RecommendationGetParam param) {
        recommendationService.toggleIsTop(param);
    }

    @ApiOperation(value = "发布最新推荐", notes = "发布最新推荐")
    @PreAuthorize("hasAuthority('mdc:recommendation:edit')")
    @ApiMapping(value = "backend.comvita.recommendation.info.release", method = RequestMethod.POST, permission = true)
    public void releaseRecommendation(RecommendationGetParam param) {
        recommendationService.releaseRecommendation(param);
    }

    @ApiOperation(value = "获取最新推荐详情", notes = "获取最新推荐详情")
    @PreAuthorize("hasAuthority('mdc:recommendation:get')")
    @ApiMapping(value = "backend.comvita.recommendation.info.get", method = RequestMethod.POST, permission = true)
    public RecommendationResult getRecommendation(RecommendationGetParam param) {
        return recommendationService.getRecommendation(param);
    }

    @ApiOperation(value = "最新推荐统计分析", notes = "最新推荐统计分析")
    @PreAuthorize("hasAuthority('mdc:recommendation:list')")
    @ApiMapping(value = "backend.comvita.recommendation.eventAnalysis.get", method = RequestMethod.POST, permission = true)
    public List<MemberEventAnalysisResult> getRecommendationEventAnalysis(EventAnalysisGetParam param) {
        return recommendationService.getEventAnalysis(param);
    }

    @ApiOperation(value = "最新推荐统计概况", notes = "最新推荐统计概况")
    @PreAuthorize("hasAuthority('mdc:recommendation:list')")
    @ApiMapping(value = "backend.comvita.recommendation.eventStatistics.get", method = RequestMethod.POST, permission = true)
    public EventStatisticsResult getEventStatistics(EventStatisticsParam param) {
        return recommendationService.getEventStatistics(param);
    }
}
