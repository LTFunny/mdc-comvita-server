package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.ticket.TicketOrderInfo;
import com.aquilaflycloud.mdc.param.ticket.*;
import com.aquilaflycloud.mdc.result.ticket.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 订单信息服务类
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-19
 */
public interface TicketOrderInfoService {

    /**
     * 微信小程序创建订单
     *
     * @param param
     */
    BaseResult<String> wechatCreateOrder(OrderInfoWechatCreateParam param);

    /**
     * 微信小程序创建并支付订单
     *
     * @param param
     * @return
     */
    void wechatCreateAndPayOrder(OrderInfoWechatCreateAndPayParam param);

    /**
     * 释放未支付的订单
     *
     * @param param
     */
    void wechatReleaseOrder(OrderInfoWechatReleaseParam param);

    /**
     * 微信小程序支付订单
     *
     * @param param
     */
    void wechatPayOrder(OrderInfoWechatPayParam param) throws IOException;


    /**
     * 微信小程序订单退款
     *
     * @param param
     */
    void wechatRefundOrder(OrderInfoWechatRefundParam param);

    /**
     * 获取当前会员订单列表
     * @return
     */
    List<WechatTicketOrderInfoListResult> wechatOrderInfos();

    /**
     * 根据订单id获取订单详情
     * @param param
     * @return
     */
    WechatGetOrderInfoByIdResult wechatGetOrderInfoById(OrderInfoWechatGetOrderInfoByIdParam param);

    /**
     * 分页获取订单信息
     * @param param
     * @return
     */
    IPage<TicketOrderInfo> pageOrderInfo(OrderInfoPageParam param);

    /**
     * 获取订单详情
     * @param param
     * @return
     */
    TicketOrderInfoResult getOrderInfo(OrderInfoGetParam param);

    /**
     * 获取付款微信支付参数
     * @param param
     * @return
     */
    BaseResult<String> wechatPrePay(OrderInfoWechatPrePayParam param);

    /**
     * 退款更新订单信息
     * @param param
     * @return
     */
    void wechatRefundUpdateOrderInfo(OrderInfoWechatUpdateParam param);

    IPage<TicketOrderResult> pageRefundOrder(OrderRefundPageParam param);

    TicketOrderResult getRefundDetail(OrderRefundGetParam param);

    TicketVerificateResult getVerificatedTicket(OrderInfoGetParam param);

    List<TicketVerificateChartResult> getUseCntByDate(TicketVerificatePageParam param);

    TicketStatisticResult verificateStatistic(TicketVerificatePageParam param);

    /**
     * 获取会员最近订单的下单姓名和手机号
     * @return
     */
    WechatTicketOrderCustInfoResult wechatGetOrderCustInfo();

    IPage<TicketVerificateOrderResult> pageVerificatedOrder(OrderVerificatePageParam param);
}
