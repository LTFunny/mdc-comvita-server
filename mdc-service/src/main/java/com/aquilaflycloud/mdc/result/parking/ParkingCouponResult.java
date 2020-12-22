package com.aquilaflycloud.mdc.result.parking;

import com.aquilaflycloud.mdc.model.parking.ParkingCoupon;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ParkingCouponResult
 *
 * @author star
 * @date 2020-01-13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ParkingCouponResult extends ParkingCoupon {
    @ApiModelProperty(value = "领取限制内容")
    private CouponLimit receiveLimit;

    @ApiModelProperty(value = "使用限制内容")
    private CouponLimit useLimit;
}
