package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.parking.CouponTypeEnum;
import com.aquilaflycloud.mdc.enums.parking.EffectiveTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * GoodsCouponAddParam
 *
 * @author star
 * @date 2020-03-21
 */
@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "effectiveType", fieldValue = "CUSTOM", notNullFieldName = "effectiveStartTime", message = "有效开始时间不能为空"),
        @AnotherFieldHasValue(fieldName = "effectiveType", fieldValue = "CUSTOM", notNullFieldName = "effectiveEndTime", message = "有效结束时间不能为空"),
})
@Data
public class GoodsParkingCouponAddParam implements Serializable {
    @ApiModelProperty(value = "优惠类型(parking.CouponTypeEnum)", required = true)
    @NotNull(message = "优惠类型不能为空")
    private CouponTypeEnum couponType;

    @ApiModelProperty(value = "优惠值")
    @DecimalMin(value = "0.01", message = "优惠值不能小于0.01")
    private BigDecimal couponWorth;

    @ApiModelProperty(value = "优惠券使用说明")
    private String couponRemark;

    @ApiModelProperty(value = "有效类型(parking.EffectiveTypeEnum)", required = true)
    @NotNull(message = "有效类型不能为空")
    private EffectiveTypeEnum effectiveType;

    @ApiModelProperty(value = "有效开始时间")
    private Date effectiveStartTime;

    @ApiModelProperty(value = "有效结束时间")
    private Date effectiveEndTime;
}


