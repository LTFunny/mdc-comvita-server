package com.aquilaflycloud.mdc.result.exchange;

import com.aquilaflycloud.mdc.model.easypay.EasypayPaymentRecord;
import com.aquilaflycloud.mdc.model.easypay.EasypayRefundRecord;
import com.aquilaflycloud.mdc.model.exchange.ExchangeOrder;
import com.aquilaflycloud.mdc.model.exchange.ExchangeOrderOperateRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class OrderResult extends ExchangeOrder {
    @ApiModelProperty(value = "优惠券信息")
    private GoodsCouponResult coupon;

    @ApiModelProperty(value = "支付信息")
    private List<EasypayPaymentRecord> paymentRecordList;

    @ApiModelProperty(value = "退款信息")
    private List<EasypayRefundRecord> refundRecordList;

    @ApiModelProperty(value = "操作记录")
    private List<ExchangeOrderOperateRecord> operateRecordList;
}
