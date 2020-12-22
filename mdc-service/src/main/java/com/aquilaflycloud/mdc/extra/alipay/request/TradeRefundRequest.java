package com.aquilaflycloud.mdc.extra.alipay.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TradeRefundRequest
 *
 * @author star
 * @date 2020-04-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TradeRefundRequest extends AlipayBaseRequest {
    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 需要退款的金额
     */
    private String refundAmount;

    /**
     * 退款的原因说明
     */
    private String refundReason;

    /**
     * 标识一次退款请求
     */
    private String outRequestNo;
}
