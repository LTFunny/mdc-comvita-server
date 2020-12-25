package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.apply.ApplyActivity;
import com.aquilaflycloud.mdc.model.apply.ApplyMemberRecord;
import com.aquilaflycloud.mdc.param.apply.ApplyGetParam;
import com.aquilaflycloud.mdc.param.apply.ApplyPageParam;
import com.aquilaflycloud.mdc.param.apply.ApplyRecordPageParam;
import com.aquilaflycloud.mdc.param.apply.RecordAddParam;
import com.aquilaflycloud.mdc.result.apply.ApplyActivityDetailResult;
import com.aquilaflycloud.mdc.result.apply.ApplyMemberRecordResult;
import com.aquilaflycloud.mdc.result.apply.ApplyResult;
import com.aquilaflycloud.mdc.service.ApplyActivityService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ApplyActivityApi
 *
 * @author star
 * @date 2020-02-29
 */
@RestController
@Api(tags = "报名活动接口")
public class ApplyActivityApi {

    @Resource
    private ApplyActivityService applyActivityService;

    @ApiOperation(value = "获取报名活动列表", notes = "获取报名活动列表")
    @ApiMapping(value = "comvita.apply.info.page", method = RequestMethod.POST)
    public IPage<ApplyResult> pageApplyActivity(ApplyPageParam param) {
        return applyActivityService.pageApplyActivity(param);
    }

    @ApiOperation(value = "获取报名活动详情", notes = "获取报名活动详情")
    @ApiMapping(value = "comvita.apply.info.get", method = RequestMethod.POST)
    public ApplyActivityDetailResult getApplyActivity(ApplyGetParam param) {
        return applyActivityService.getApplyActivity(param);
    }

    @ApiOperation(value = "新增报名记录", notes = "新增报名记录,会员报名活动")
    @ApiMapping(value = "comvita.apply.record.add", method = RequestMethod.POST)
    public void addApplyRecord(RecordAddParam param) {
        applyActivityService.addApplyRecord(param);
    }

    @ApiOperation(value = "获取报名记录列表", notes = "获取报名记录列表")
    @ApiMapping(value = "comvita.apply.record.page", method = RequestMethod.POST)
    public IPage<ApplyMemberRecord> pageApplyRecord(ApplyRecordPageParam param) {
        return applyActivityService.pageApplyRecord(param);
    }

    @ApiOperation(value = "获取报名活动记录列表", notes = "获取报名活动记录列表")
    @ApiMapping(value = "comvita.apply.memberRecord.page", method = RequestMethod.POST)
    public IPage<ApplyMemberRecordResult> pageApplyMemberRecord(PageParam<ApplyMemberRecord> param) {
        return applyActivityService.pageApplyMemberRecord(param);
    }
}
