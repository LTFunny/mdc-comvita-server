package com.aquilaflycloud.mdc.result.coupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * StatisticsResult
 *
 * @author star
 * @date 2020-03-09
 */
@Data
public class StatisticsResult implements Serializable {
    @ApiModelProperty(value = "优惠券总数")
    private Integer couponCount;

    @ApiModelProperty(value = "待审核数量")
    private Integer pendingCount;

    @ApiModelProperty(value = "已领取数量")
    private Integer receiveCount;

    @ApiModelProperty(value = "已核销数量")
    private Integer verificateCount;

    @ApiModelProperty(value = "商户优惠券数量")
    private Integer shopCouponCount;
}
