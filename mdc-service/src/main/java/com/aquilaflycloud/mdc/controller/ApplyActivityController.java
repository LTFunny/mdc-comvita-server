package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.apply.ApplyMemberRecord;
import com.aquilaflycloud.mdc.param.apply.*;
import com.aquilaflycloud.mdc.result.apply.ApplyDetailResult;
import com.aquilaflycloud.mdc.result.apply.ApplyResult;
import com.aquilaflycloud.mdc.result.apply.StatisticsResult;
import com.aquilaflycloud.mdc.service.ApplyActivityService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ApplyActivityController
 *
 * @author star
 * @date 2020-02-27
 */
@RestController
@Api(tags = "报名活动管理")
public class ApplyActivityController {
    @Resource
    private ApplyActivityService applyActivityService;

    @ApiOperation(value = "获取报名活动列表(分页)", notes = "获取报名活动列表(分页)")
    @PreAuthorize("hasAuthority('mdc:apply:list')")
    @ApiMapping(value = "backend.mdc.apply.info.page", method = RequestMethod.POST, permission = true)
    public IPage<ApplyResult> pageApply(ApplyPageParam param) {
        return applyActivityService.pageApply(param);
    }

    @ApiOperation(value = "获取报名活动详情", notes = "获取报名活动详情")
    @PreAuthorize("hasAuthority('mdc:apply:get')")
    @ApiMapping(value = "backend.mdc.apply.info.get", method = RequestMethod.POST, permission = true)
    public ApplyDetailResult getApply(ApplyGetParam param) {
        return applyActivityService.getApply(param);
    }

    @ApiOperation(value = "新增报名活动", notes = "新增报名活动")
    @PreAuthorize("hasAuthority('mdc:apply:add')")
    @ApiMapping(value = "backend.mdc.apply.info.add", method = RequestMethod.POST, permission = true)
    public void addApply(ApplyAddParam param) {
        applyActivityService.addApply(param);
    }

    @ApiOperation(value = "编辑报名活动", notes = "编辑报名活动")
    @PreAuthorize("hasAuthority('mdc:apply:edit')")
    @ApiMapping(value = "backend.mdc.apply.info.edit", method = RequestMethod.POST, permission = true)
    public void editApply(ApplyEditParam param) {
        applyActivityService.editApply(param);
    }

    @ApiOperation(value = "是否置顶报名活动", notes = "编辑报名活动置顶状态,是/否")
    @PreAuthorize("hasAuthority('mdc:apply:edit')")
    @ApiMapping(value = "backend.mdc.apply.top.toggle", method = RequestMethod.POST, permission = true)
    public void toggleApplyTop(ApplyGetParam param) {
        applyActivityService.toggleTop(param);
    }

    @ApiOperation(value = "启用/停用报名活动", notes = "编辑报名活动状态,启用/停用")
    @PreAuthorize("hasAuthority('mdc:apply:edit')")
    @ApiMapping(value = "backend.mdc.apply.state.toggle", method = RequestMethod.POST, permission = true)
    public void toggleApplyState(ApplyGetParam param) {
        applyActivityService.toggleState(param);
    }

    @ApiOperation(value = "获取活动报名概况统计", notes = "获取活动报名概况统计")
    @PreAuthorize("hasAuthority('mdc:apply:list')")
    @ApiMapping(value = "backend.mdc.apply.statistics.get", method = RequestMethod.POST, permission = true)
    public StatisticsResult getStatistics() {
        return applyActivityService.getStatistics();
    }

    @ApiOperation(value = "审批报名记录", notes = "审批报名记录")
    @PreAuthorize("hasAuthority('mdc:applyRecord:audit')")
    @ApiMapping(value = "backend.mdc.apply.record.audit", method = RequestMethod.POST, permission = true)
    public void auditRecord(RecordAuditParam param) {
        applyActivityService.auditRecord(param);
    }

    @ApiOperation(value = "批量审批报名记录", notes = "批量审批报名记录")
    @PreAuthorize("hasAuthority('mdc:applyRecord:audit')")
    @ApiMapping(value = "backend.mdc.apply.record.batchAudit", method = RequestMethod.POST, permission = true)
    public void batchAuditRecord(RecordBatchAuditParam param) {
        applyActivityService.batchAuditRecord(param);
    }

    @ApiOperation(value = "获取报名记录列表(分页)", notes = "获取报名记录列表(分页)")
    @PreAuthorize("hasAuthority('mdc:applyRecord:list')")
    @ApiMapping(value = "backend.mdc.apply.record.page", method = RequestMethod.POST, permission = true)
    public IPage<ApplyMemberRecord> pageRecord(RecordPageParam param) {
        return applyActivityService.pageRecord(param);
    }
}
