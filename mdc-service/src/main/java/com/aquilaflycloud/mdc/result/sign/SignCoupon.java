package com.aquilaflycloud.mdc.result.sign;

import com.aquilaflycloud.mdc.enums.coupon.CouponStateEnum;
import com.aquilaflycloud.mdc.enums.coupon.CouponTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SignCoupon {
    @ApiModelProperty(value = "优惠券编码")
    private String couponCode;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "优惠券类型")
    private CouponTypeEnum couponType;

    @ApiModelProperty(value = "优惠券库存")
    private Integer inventory;

    @ApiModelProperty(value = "累计已领取数量")
    private Integer receiveCount;

    @ApiModelProperty(value = "状态")
    private CouponStateEnum state;
}
