package com.aquilaflycloud.mdc.extra.alipay.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TradeQueryRequest
 *
 * @author star
 * @date 2020-05-20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TradeQueryRequest extends AlipayBaseRequest {
    /**
     * 商户订单号
     */
    private String outTradeNo;
}
