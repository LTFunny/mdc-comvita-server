package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.aquilaflycloud.mdc.model.pre.PreRefundOrderInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.*;
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
    public IPage<PreOrderInfo> pageAdministrationList(AdministrationListParam param) {
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
    public IPage<PreRefundOrderInfo> pageAfterSalesList(AdministrationListParam param) {
        return preOrderAdministrationService.pageOrderInfoList(param);
    }
    @ApiOperation(value = "售后订单详情", notes = "售后订单详情")
    @ApiMapping(value = "backend.comvita.after.details", method = RequestMethod.POST)
    public AfterSalesDetailsResult getAfterOrderDetails (OrderDetailsParam param) {
        return preOrderAdministrationService.getAfterOrderDetails(param);
    }

    @ApiOperation(value = "待发货订单列表", notes = "待发货订单列表")
    @ApiMapping(value = "backend.comvita.ready.sales.page", method = RequestMethod.POST)
    public IPage<PreOrderGoods> pagereadySalesList(ReadyListParam param) {
        return preOrderAdministrationService.pagereadySalesList(param);
    }
}
