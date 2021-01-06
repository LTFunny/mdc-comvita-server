package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.param.pre.AdministrationListParam;
import com.aquilaflycloud.mdc.param.pre.ChangeGoodsInfoParam;
import com.aquilaflycloud.mdc.param.pre.InputOrderNumberParam;
import com.aquilaflycloud.mdc.param.pre.OrderDetailsParam;
import com.aquilaflycloud.mdc.result.pre.AdministrationDetailsResult;
import com.aquilaflycloud.mdc.result.pre.AdministrationPageResult;
import com.aquilaflycloud.mdc.result.pre.RefundOrderInfoPageResult;
import com.aquilaflycloud.mdc.service.PreOrderAdministrationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author zly
 */
@RestController
@Api(tags = "订单管理")
public class PreOrderAdministrationController {

    @Resource
    private PreOrderAdministrationService preOrderAdministrationService;

    @ApiOperation(value = "订单管理列表", notes = "订单管理列表")
    @ApiMapping(value = "backend.comvita.administration.page", method = RequestMethod.POST)
    public IPage<AdministrationPageResult> pageAdministrationList(AdministrationListParam param) {
        return preOrderAdministrationService.pageAdministrationList(param);
    }


    @ApiOperation(value = "录入快递单号", notes = "录入快递单号")
    @ApiMapping(value = "backend.comvita.input.order.number", method = RequestMethod.POST)
    public void inputOrderNumber (InputOrderNumberParam param) {
        preOrderAdministrationService.inputOrderNumber(param);
    }
    @ApiOperation(value = "订单详情", notes = "订单详情")
    @ApiMapping(value = "backend.comvita.order.details", method = RequestMethod.POST)
    public AdministrationDetailsResult getOrderDetails (OrderDetailsParam param) {
        return preOrderAdministrationService.getOrderDetails(param);
    }
    @ApiOperation(value = "售后订单列表", notes = "售后订单列表")
    @ApiMapping(value = "backend.comvita.after.sales.page", method = RequestMethod.POST)
    public IPage<RefundOrderInfoPageResult> pageAfterSalesList(AdministrationListParam param) {
        return preOrderAdministrationService.pageOrderInfoList(param);
    }
}
