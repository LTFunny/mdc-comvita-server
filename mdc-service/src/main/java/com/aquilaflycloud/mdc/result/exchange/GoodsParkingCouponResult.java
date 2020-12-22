package com.aquilaflycloud.mdc.result.exchange;

import com.aquilaflycloud.mdc.enums.parking.CouponTypeEnum;
import com.aquilaflycloud.mdc.enums.parking.EffectiveTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * GoodsParkingCouponAddParam
 *
 * @author star
 * @date 2020-03-23
 */
@Data
public class GoodsParkingCouponResult implements Serializable {
    @ApiModelProperty(value = "优惠类型")
    private CouponTypeEnum couponType;

    @ApiModelProperty(value = "优惠值")
    private BigDecimal couponWorth;

    @ApiModelProperty(value = "优惠券使用说明")
    private String couponRemark;

    @ApiModelProperty(value = "有效类型")
    private EffectiveTypeEnum effectiveType;

    @ApiModelProperty(value = "有效开始时间")
    private Date effectiveStartTime;

    @ApiModelProperty(value = "有效结束时间")
    private Date effectiveEndTime;
}


