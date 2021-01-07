package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.param.pre.PreConfirmOrderParam;
import com.aquilaflycloud.mdc.param.pre.PreOrderCardGetParam;
import com.aquilaflycloud.mdc.param.pre.PreOrderRefundParam;
import com.aquilaflycloud.mdc.param.pre.PreOrderVerificationParam;
import com.aquilaflycloud.mdc.result.pre.PreOrderGoodsGetResult;
import com.aquilaflycloud.mdc.service.PreOrderInfoService;
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

    @ApiOperation(value = "对订单进行确认", notes = "对订单进行确认")
    //@PreAuthorize("hasAuthority('mdc:confirm:validation')")
    @ApiMapping(value = "backend.comvita.order.info.confirm.validation", method = RequestMethod.POST)
    public void validationConfirmOrder(PreConfirmOrderParam param) {
        orderInfoService.validationConfirmOrder(param);
    }


    @ApiOperation(value = "核销订单", notes = "核销订单")
    @PreAuthorize("hasAuthority('mdc:order:verification')")
    @ApiMapping(value = "backend.comvita.order.verification", method = RequestMethod.POST)
    public void verificationOrder(PreOrderVerificationParam param) {
        orderInfoService.verificationOrder(param);
    }


    @ApiOperation(value = "核销提货卡详情", notes = "核销提货卡详情")
    @PreAuthorize("hasAuthority('mdc:orderCard:get')")
    @ApiMapping(value = "backend.comvita.order.card.get", method = RequestMethod.POST)
    public PreOrderGoodsGetResult orderCardGetInfo(PreOrderCardGetParam param) {
        return orderInfoService.orderCardGetInfo(param);
    }


    @ApiOperation(value = "登记订单退货", notes = "登记订单退货")
    @PreAuthorize("hasAuthority('mdc:order:refund')")
    @ApiMapping(value = "backend.comvita.order.info.refund", method = RequestMethod.POST)
    public void refundOrder(PreOrderRefundParam param) {
        orderInfoService.refundOrder(param);
    }

}
