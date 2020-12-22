package com.aquilaflycloud.mdc.extra.alipay.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TradeCreateRequest
 *
 * @author star
 * @date 2020-04-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TradeCreateRequest extends AlipayBaseRequest {
    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 订单总金额
     */
    private String totalAmount;

    /**
     * 支付宝userId
     */
    private String buyerId;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 商品描述信息
     */
    private String body;

    /**
     * 最晚付款时间
     */
    private String timeoutExpress;

    /**
     * 系统商编号
     */
    private String sysServiceProviderId;
}
