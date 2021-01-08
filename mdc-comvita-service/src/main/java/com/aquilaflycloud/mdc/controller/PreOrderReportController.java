package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.param.pre.ReportFormParam;
import com.aquilaflycloud.mdc.result.pre.ReportGuidePageResult;
import com.aquilaflycloud.mdc.result.pre.ReportOrderPageResult;
import com.aquilaflycloud.mdc.service.PreOrderAdministrationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author zly
 */
@RestController
@Api(tags = "统计报表")
public class PreOrderReportController {

    @Resource
    private PreOrderAdministrationService preOrderAdministrationService;

    @ApiOperation(value = "订单报表", notes = "订单报表")
    @ApiMapping(value = "backend.comvita.order.report.page", method = RequestMethod.POST, permission = true)
    public IPage<ReportOrderPageResult> pageOrderReportList(ReportFormParam param) {
        return preOrderAdministrationService.pageOrderReportList(param);
    }

    @ApiOperation(value = "导购员绩效", notes = "导购员绩效")
    @ApiMapping(value = "backend.comvita.achievements.guide", method = RequestMethod.POST, permission = true)
    public IPage<ReportGuidePageResult> achievementsGuide (ReportFormParam param) {
        return  preOrderAdministrationService.achievementsGuide(param);
    }

}
