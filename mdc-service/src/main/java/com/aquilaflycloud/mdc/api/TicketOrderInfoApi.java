package com.aquilaflycloud.mdc.api;


import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.param.ticket.*;
import com.aquilaflycloud.mdc.result.ticket.WechatGetOrderInfoByIdResult;
import com.aquilaflycloud.mdc.result.ticket.WechatTicketOrderInfoListResult;
import com.aquilaflycloud.mdc.service.TicketOrderInfoService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 订单信息相关接口
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-19
 */
@RestController
@Api(tags = "订单信息相关接口")
public class TicketOrderInfoApi {
    @Resource
    private TicketOrderInfoService ticketOrderInfoService;

    @ApiOperation(value = "微信小程序获取会员订单列表", notes = "微信小程序获取会员订单列表")
    @ApiMapping(value = "comvita.ticket.orderInfo.list", method = RequestMethod.POST)
    public List<WechatTicketOrderInfoListResult> wechatOrderInfos() {
        return ticketOrderInfoService.wechatOrderInfos();
    }

    @ApiOperation(value = "获取会员最近下单的顾客信息和手机号", notes = "获取会员最近下单的顾客信息和手机号")
    @ApiMapping(value = "comvita.ticket.orderInfo.getCustInfo", method = RequestMethod.POST)
    public WechatTicketOrderCustInfoResult wechatGetOrderCustInfo() {
        return ticketOrderInfoService.wechatGetOrderCustInfo();
    }

    @ApiOperation(value = "微信小程序创建订单", notes = "微信小程序创建订单")
    @ApiMapping(value = "comvita.ticket.orderInfo.create", method = RequestMethod.POST)
    public BaseResult<String> wechatCreateOrder(OrderInfoWechatCreateParam param) {
        return ticketOrderInfoService.wechatCreateOrder(param);
    }

    @ApiOperation(value = "微信小程序支付订单", notes = "微信小程序支付订单")
    @ApiMapping(value = "comvita.ticket.orderInfo.pay", method = RequestMethod.POST)
    public BaseResult<String> wechatPrePay(OrderInfoWechatPrePayParam param) throws IOException {
        return ticketOrderInfoService.wechatPrePay(param);
    }

    @ApiOperation(value = "微信小程序释放订单(当创建订单后未支付状态)", notes = "微信小程序释放订单(当创建订单后未支付状态)")
    @ApiMapping(value = "comvita.ticket.orderInfo.release", method = RequestMethod.POST)
    public void wechatReleaseOrder(OrderInfoWechatReleaseParam param) {
        ticketOrderInfoService.wechatReleaseOrder(param);
    }

    /*@ApiOperation("微信小程序创建并支付订单")
    @ApiMapping(value = "comvita.ticket.orderInfo.createAndPay", method = RequestMethod.POST)
    public void wechatCreateAndPayOrder(OrderInfoWechatCreateAndPayParam param) {
        ticketOrderInfoService.wechatCreateAndPayOrder(param);
    }*/

    @ApiOperation(value = "微信小程序退款订单", notes = "微信小程序退款订单")
    @ApiMapping(value = "comvita.ticket.orderInfo.refund", method = RequestMethod.POST)
    public void wechatRefundOrder(OrderInfoWechatRefundParam param) {
        ticketOrderInfoService.wechatRefundOrder(param);
    }

    @ApiOperation(value = "微信小程序根据id获取订单", notes = "微信小程序根据id获取订单")
    @ApiMapping(value = "comvita.ticket.orderInfo.getById", method = RequestMethod.POST)
    public WechatGetOrderInfoByIdResult wechatGetOrderInfoById(OrderInfoWechatGetOrderInfoByIdParam param) {
        return ticketOrderInfoService.wechatGetOrderInfoById(param);
    }
}
