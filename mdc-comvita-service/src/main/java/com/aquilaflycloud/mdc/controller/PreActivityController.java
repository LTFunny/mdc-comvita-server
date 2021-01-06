package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.PreActivityAnalysisResult;
import com.aquilaflycloud.mdc.result.pre.PreActivityDetailResult;
import com.aquilaflycloud.mdc.service.PreActivityService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * PreActivityController
 * @author linkq
 */
@RestController
@Api(tags = "销售活动")
public class PreActivityController {

    @Resource
    private PreActivityService preActivityService;

    @ApiOperation(value = "销售活动分页信息", notes = "销售活动分页信息")
    @PreAuthorize("hasAuthority('mdc:pre:activity:page')")
    @ApiMapping(value = "backend.comvita.pre.activity.page", method = RequestMethod.POST, permission = true)
    public IPage<PreActivityInfo> page(PreActivityPageParam param) {
        return preActivityService.page(param);
    }

    @ApiOperation(value = "活动新增", notes = "活动新增")
    @PreAuthorize("hasAuthority('mdc:pre:activity:add')")
    @ApiMapping(value = "backend.comvita.pre.activity.add", method = RequestMethod.POST, permission = true)
    public void add(PreActivityAddParam param) {
        preActivityService.add(param);
    }

    @ApiOperation(value = "活动详情", notes = "活动详情")
    @PreAuthorize("hasAuthority('mdc:pre:activity:get')")
    @ApiMapping(value = "backend.comvita.pre.activity.get", method = RequestMethod.POST, permission = true)
    public PreActivityDetailResult get(PreActivityGetParam param) {
        return preActivityService.get(param);
    }

    @ApiOperation(value = "活动编辑", notes = "活动编辑")
    @PreAuthorize("hasAuthority('mdc:pre:activity:update')")
    @ApiMapping(value = "backend.comvita.pre.activity.update", method = RequestMethod.POST, permission = true)
    public void update(PreActivityUpdateParam param) {
        preActivityService.update(param);
    }

    @ApiOperation(value = "活动下架", notes = "活动下架")
    @PreAuthorize("hasAuthority('mdc:pre:activity:cancel')")
    @ApiMapping(value = "backend.comvita.pre.activity.cancel", method = RequestMethod.POST, permission = true)
    public void cancel(PreActivityCancelParam param) {
        preActivityService.cancel(param);
    }

    /**
     * 获取活动概况
     * @param param
     * @return
     */
    @ApiOperation(value = "活动概况", notes = "活动概况")
    @PreAuthorize("hasAuthority('mdc:pre:activity:analyse')")
    @ApiMapping(value = "backend.comvita.pre.activity.analyse", method = RequestMethod.POST, permission = true)
    public PreActivityAnalysisResult analyse(PreActivityAnalysisParam param) {
        return preActivityService.analyse(param);
    }

}
