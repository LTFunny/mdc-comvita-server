package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.sign.OfflineSignActivity;
import com.aquilaflycloud.mdc.param.sign.*;
import com.aquilaflycloud.mdc.result.sign.OfflineSignRecordResult;
import com.aquilaflycloud.mdc.result.sign.SignResult;
import com.aquilaflycloud.mdc.service.OfflineSignActivityService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * OfflineSignActivityController
 *
 * @author star
 * @date 2020-05-07
 */
@RestController
@Api(tags = "线下打卡活动管理")
public class OfflineSignActivityController {
    @Resource
    private OfflineSignActivityService offlineSignActivityService;

    @ApiOperation(value = "获取打卡活动列表(分页)", notes = "获取打卡活动列表(分页)")
    @PreAuthorize("hasAuthority('mdc:offlineSign:list')")
    @ApiMapping(value = "backend.mdc.offlineSign.info.page", method = RequestMethod.POST, permission = true)
    public IPage<OfflineSignActivity> pageSign(SignPageParam param) {
        return offlineSignActivityService.pageSign(param);
    }

    @ApiOperation(value = "获取打卡活动详情", notes = "获取打卡活动详情")
    @PreAuthorize("hasAuthority('mdc:offlineSign:get')")
    @ApiMapping(value = "backend.mdc.offlineSign.info.get", method = RequestMethod.POST, permission = true)
    public SignResult getSign(SignGetParam param) {
        return offlineSignActivityService.getSign(param);
    }

    @ApiOperation(value = "新增打卡活动", notes = "新增打卡活动")
    @PreAuthorize("hasAuthority('mdc:offlineSign:add')")
    @ApiMapping(value = "backend.mdc.offlineSign.info.add", method = RequestMethod.POST, permission = true)
    public void addSign(SignAddParam param) {
        offlineSignActivityService.addSign(param);
    }

    @ApiOperation(value = "编辑打卡活动", notes = "编辑打卡活动")
    @PreAuthorize("hasAuthority('mdc:offlineSign:edit')")
    @ApiMapping(value = "backend.mdc.offlineSign.info.edit", method = RequestMethod.POST, permission = true)
    public void editSign(SignEditParam param) {
        offlineSignActivityService.editSign(param);
    }

    @ApiOperation(value = "启用/停用打卡活动", notes = "编辑打卡活动状态,启用/停用")
    @PreAuthorize("hasAuthority('mdc:offlineSign:edit')")
    @ApiMapping(value = "backend.mdc.offlineSign.state.toggle", method = RequestMethod.POST, permission = true)
    public void toggleSignState(SignGetParam param) {
        offlineSignActivityService.toggleState(param);
    }

    @ApiOperation(value = "获取打卡记录列表(分页)", notes = "获取打卡记录列表(分页)")
    @PreAuthorize("hasAuthority('mdc:offlineSignRecord:list')")
    @ApiMapping(value = "backend.mdc.offlineSign.record.page", method = RequestMethod.POST, permission = true)
    public IPage<OfflineSignRecordResult> pageRecord(RecordPageParam param) {
        return offlineSignActivityService.pageRecord(param);
    }
}
