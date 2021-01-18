package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.aquilaflycloud.mdc.model.pre.PreRefundOrderInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.param.system.FileUploadParam;
import com.aquilaflycloud.mdc.result.pre.*;
import com.aquilaflycloud.mdc.service.PreOrderAdministrationService;
import com.aquilaflycloud.mdc.service.PreOrderInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author pengyongliang
 * @Date 2020/12/31 14:42
 * @Version 1.0
 */
@RestController
@Api(tags = "订单接口")
public class PreOrderInfoController {

    @Resource
    private PreOrderInfoService orderInfoService;
    @Resource
    private PreOrderAdministrationService preOrderAdministrationService;

    @ApiOperation(value = "对订单进行确认", notes = "对订单进行确认")
    //@PreAuthorize("hasAuthority('mdc:confirm:validation')")
    @ApiMapping(value = "backend.comvita.order.info.confirm.validation", method = RequestMethod.POST, permission = true)
    public void validationConfirmOrder(PreConfirmOrderParam param) {
        orderInfoService.validationConfirmOrder(param);
    }


    @ApiOperation(value = "核销订单", notes = "核销订单")
//    @PreAuthorize("hasAuthority('mdc:order:verification')")
    @ApiMapping(value = "backend.comvita.order.verification", method = RequestMethod.POST, permission = true)
    public void verificationOrder(PreOrderVerificationParam param) {
        orderInfoService.verificationOrder(param);
    }


    @ApiOperation(value = "核销提货卡详情", notes = "核销提货卡详情")
//    @PreAuthorize("hasAuthority('mdc:orderCard:get')")
    @ApiMapping(value = "backend.comvita.order.card.get", method = RequestMethod.POST, permission = true)
    public PreOrderGoodsGetResult orderCardGetInfo(PreOrderCardGetParam param) {
        return orderInfoService.orderCardGetInfo(param);
    }

    @ApiOperation(value = "登记订单退货", notes = "登记订单退货")
//    @PreAuthorize("hasAuthority('mdc:order:refund')")
    @ApiMapping(value = "backend.comvita.order.info.refund", method = RequestMethod.POST, permission = true)
    public void refundOrder(PreOrderRefundParam param) {
        orderInfoService.refundOrder(param);
    }

    @ApiOperation(value = "获取预售订单统计", notes = "获取预售订单统计")
    @ApiMapping(value = "backend.comvita.preOrder.statistics.get", method = RequestMethod.POST, permission = true)
    public PreOrderStatisticsResult getPreOderStatistics(PreOrderListParam param) {
        return preOrderAdministrationService.getPreOderStatistics(param);
    }

    @ApiOperation(value = "订单管理列表", notes = "订单管理列表")
    @ApiMapping(value = "backend.comvita.administration.page", method = RequestMethod.POST, permission = true)
    public IPage<PreOrderInfo> pagePreOder(PreOrderPageParam param) {
        return preOrderAdministrationService.pagePreOder(param);
    }
    @ApiOperation(value = "移动端订单列表", notes = "移动端订单列表")
    @ApiMapping(value = "backend.comvita.administration.mobile.page", method = RequestMethod.POST, permission = true)
    public IPage<PreOrderInfo> pageMobilePreOder(PreOrderPageParam param) {
        return preOrderAdministrationService.pageMobilePreOder(param);
    }
    @ApiOperation(value = "录入快递单号", notes = "录入快递单号")
    @ApiMapping(value = "backend.comvita.input.order.number", method = RequestMethod.POST, permission = true)
    public void inputOrderNumber (InputOrderNumberParam param) {
        preOrderAdministrationService.inputOrderNumber(param);
    }

    @ApiOperation(value = "批量导入快递单号", notes = "批量导入快递单号")
    @PreAuthorize("hasAuthority('mdc:pre:order:info:batchImport')")
    @ApiMapping(value = "backend.comvita.import.order.code", method = RequestMethod.POST, permission = true)
    public void importOrderCode (FileUploadParam param) {
        preOrderAdministrationService.importOrderCode(param);
    }

    @ApiOperation(value = "订单详情", notes = "订单详情")
    @ApiMapping(value = "backend.comvita.order.details", method = RequestMethod.POST, permission = true)
    public AdministrationDetailsResult getOrderDetails (OrderDetailsParam param) {
        return preOrderAdministrationService.getOrderDetails(param);
    }

    @ApiOperation(value = "售后订单列表", notes = "售后订单列表")
    @ApiMapping(value = "backend.comvita.after.sales.page", method = RequestMethod.POST, permission = true)
    public IPage<PreRefundOrderInfo> pageAfterSalesList(PreRefundOrderListParam param) {
        return preOrderAdministrationService.pageOrderInfoList(param);
    }

    @ApiOperation(value = "售后订单详情", notes = "售后订单详情")
    @ApiMapping(value = "backend.comvita.after.details", method = RequestMethod.POST, permission = true)
    public AfterSalesDetailsResult getAfterOrderDetails (OrderDetailsParam param) {
        return preOrderAdministrationService.getAfterOrderDetails(param);
    }

    @ApiOperation(value = "待发货订单列表", notes = "待发货订单列表")
    @ApiMapping(value = "backend.comvita.ready.sales.page", method = RequestMethod.POST, permission = true)
    public IPage<PreOrderGoods> pagereadySalesList(ReadyListParam param) {
        return preOrderAdministrationService.pagereadySalesList(param);
    }

    @ApiOperation(value = "登记退货列表", notes = "登记退货列表")
    //    @PreAuthorize("hasAuthority('mdc:orderRefund.page')")
    @ApiMapping(value = "backend.comvita.order.refund.info.page", method = RequestMethod.POST, permission = true)
    public IPage<PreOrderInfoPageResult> refundOrderPage(PreOrderInfoPageParam param) {
        return orderInfoService.refundOrderPage(param);
    }

    @ApiOperation(value = "登记退货详情", notes = "登记退货详情")
    //    @PreAuthorize("hasAuthority('mdc:orderRefund.get')")
    @ApiMapping(value = "backend.comvita.order.refund.info.get", method = RequestMethod.POST, permission = true)
    public PreOrderInfoPageResult refundOrderInfoGet(PreOrderInfoGetParam param) {
        return orderInfoService.orderInfoGet(param);
    }


    @ApiOperation(value = "我的订单-导购员", notes = "我的订单-导购员")
    //    @PreAuthorize("hasAuthority('mdc:orderRefund.get')")
    @ApiMapping(value = "backend.comvita.order.my.info.page", method = RequestMethod.POST, permission = true)
    public IPage<PreOrderInfoPageResult> guideMyOrderPage(PreOrderInfoPageParam param) {
        return orderInfoService.guideMyOrderPage(param);
    }

}
