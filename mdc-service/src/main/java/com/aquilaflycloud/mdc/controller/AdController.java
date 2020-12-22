package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.model.ad.AdInfo;
import com.aquilaflycloud.mdc.param.ad.*;
import com.aquilaflycloud.mdc.result.ad.AdInfoResult;
import com.aquilaflycloud.mdc.result.ad.EventStatisticsResult;
import com.aquilaflycloud.mdc.result.ad.StatisticsResult;
import com.aquilaflycloud.mdc.result.member.MemberEventAnalysisResult;
import com.aquilaflycloud.mdc.result.member.MemberEventSexAndAgeResult;
import com.aquilaflycloud.mdc.service.AdService;
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
 * AdController
 *
 * @author star
 * @date 2019-11-18
 */
@RestController
@Api(tags = "广告管理")
public class AdController {
    @Resource
    private AdService adService;

    @ApiOperation(value = "获取广告列表(分页)", notes = "获取广告列表(分页)")
    @PreAuthorize("hasAuthority('mdc:ad:list')")
    @ApiMapping(value = "backend.mdc.ad.info.page", method = RequestMethod.POST, permission = true)
    public IPage<AdInfo> pageAd(AdPageParam param) {
        return adService.pageAd(param);
    }

    @ApiOperation(value = "获取广告详情", notes = "获取广告详情")
    @PreAuthorize("hasAuthority('mdc:ad:get')")
    @ApiMapping(value = "backend.mdc.ad.info.get", method = RequestMethod.POST, permission = true)
    public AdInfoResult getAd(AdGetParam param) {
        return adService.getAd(param);
    }

    @ApiOperation(value = "删除广告", notes = "删除停用,已过期,或审核未通过(包括待审核和驳回)的广告")
    @PreAuthorize("hasAuthority('mdc:ad:delete')")
    @ApiMapping(value = "backend.mdc.ad.info.delete", method = RequestMethod.POST, permission = true)
    public void deleteAd(AdGetParam param) {
        adService.deleteAd(param);
    }

    @ApiOperation(value = "新增广告", notes = "新增广告")
    @PreAuthorize("hasAuthority('mdc:ad:add')")
    @ApiMapping(value = "backend.mdc.ad.info.add", method = RequestMethod.POST, permission = true)
    public void addAd(AdAddParam param) {
        adService.add(param);
    }

    @ApiOperation(value = "编辑广告", notes = "编辑广告")
    @PreAuthorize("hasAuthority('mdc:ad:edit')")
    @ApiMapping(value = "backend.mdc.ad.info.edit", method = RequestMethod.POST, permission = true)
    public void editAd(AdEditParam param) {
        adService.edit(param);
    }

    @ApiOperation(value = "启用/停用广告", notes = "编辑广告状态,启用/停用")
    @PreAuthorize("hasAuthority('mdc:ad:edit')")
    @ApiMapping(value = "backend.mdc.ad.state.toggle", method = RequestMethod.POST, permission = true)
    public void toggleAdState(AdGetParam param) {
        adService.toggleState(param);
    }

    @ApiOperation(value = "获取广告概况统计", notes = "获取广告概况统计")
    @PreAuthorize("hasAuthority('mdc:ad:list')")
    @ApiMapping(value = "backend.mdc.ad.statistics.get", method = RequestMethod.POST, permission = true)
    public StatisticsResult getStatistics(AuthParam param) {
        return adService.getStatistics(param);
    }

    @ApiOperation(value = "审批广告", notes = "审批广告")
    @PreAuthorize("hasAuthority('mdc:ad:audit')")
    @ApiMapping(value = "backend.mdc.ad.info.audit", method = RequestMethod.POST, permission = true)
    public void auditAd(AdAuditParam param) {
        adService.audit(param);
    }

    @ApiOperation(value = "获取广告事件统计", notes = "获取广告事件统计")
    @PreAuthorize("hasAuthority('mdc:ad:eventGet')")
    @ApiMapping(value = "backend.mdc.ad.eventStatistics.get", method = RequestMethod.POST, permission = true)
    public EventStatisticsResult getEventStatistics(AdEventStatisticsGetParam param) {
        return adService.getAdEventStatistics(param);
    }

    @ApiOperation(value = "获取广告事件分析", notes = "获取广告事件分析")
    @PreAuthorize("hasAuthority('mdc:ad:eventGet')")
    @ApiMapping(value = "backend.mdc.ad.eventAnalysis.get", method = RequestMethod.POST, permission = true)
    public List<MemberEventAnalysisResult> getEventAnalysis(AdEventAnalysisGetParam param) {
        return adService.getAdEventAnalysis(param);
    }

    @ApiOperation(value = "获取广告事件分布数据统计", notes = "获取广告事件性别和年龄分布数据统计")
    @PreAuthorize("hasAuthority('mdc:ad:eventGet')")
    @ApiMapping(value = "backend.mdc.ad.eventDistribution.get", method = RequestMethod.POST, permission = true)
    public MemberEventSexAndAgeResult getEventDistribution(AdEventDistributionGetParam param) {
        return adService.getAdEventDistribution(param);
    }
}
