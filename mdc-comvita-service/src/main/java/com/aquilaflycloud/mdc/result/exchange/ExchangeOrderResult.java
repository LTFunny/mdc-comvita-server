package com.aquilaflycloud.mdc.result.exchange;

import com.aquilaflycloud.mdc.model.coupon.CouponMemberRel;
import com.aquilaflycloud.mdc.model.easypay.EasypayPaymentRecord;
import com.aquilaflycloud.mdc.model.easypay.EasypayRefundRecord;
import com.aquilaflycloud.mdc.model.exchange.ExchangeOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ExchangeOrderResult extends ExchangeOrder {
    @ApiModelProperty(value = "优惠券信息")
    private GoodsCouponResult coupon;

    @ApiModelProperty(value = "商品图片列表")
    private List<String> goodsImgList;

    @ApiModelProperty(value = "会员优惠券记录")
    private List<CouponMemberRel> couponRelList;

    @ApiModelProperty(value = "支付信息")
    private EasypayPaymentRecord paymentRecord;

    @ApiModelProperty(value = "退款信息")
    private EasypayRefundRecord refundRecord;

    @ApiModelProperty(value = "订单剩余有效时间(秒)")
    private Long effectiveSecond;
}
