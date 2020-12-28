package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.easypay.EasypayPaymentRecord;
import com.aquilaflycloud.mdc.param.easypay.OrderParam;
import com.aquilaflycloud.mdc.param.easypay.RefundParam;

/**
 * EasyPayService
 *
 * @author star
 * @date 2019-12-07
 */
public interface EasyPayService {
    Boolean checkOrder(EasypayPaymentRecord record);

    String orderExchange(OrderParam param);

    void refundExchangeOrder(RefundParam param);

    void finishOrder(Object notify);

    void finishRefundOrder(Object notify);
}

