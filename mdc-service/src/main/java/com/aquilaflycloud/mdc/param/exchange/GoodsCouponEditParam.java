package com.aquilaflycloud.mdc.param.exchange;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.coupon.EffectiveTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * GoodsCouponEditParam
 *
 * @author star
 * @date 2020-03-21
 */
@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "effectiveType", fieldValue = "DATERANGE", notNullFieldName = "effectiveStartTime", message = "有效开始时间不能为空"),
        @AnotherFieldHasValue(fieldName = "effectiveType", fieldValue = "DATERANGE", notNullFieldName = "effectiveEndTime", message = "有效结束时间不能为空"),
        @AnotherFieldHasValue(fieldName = "effectiveType", fieldValue = "AFTERDAYS", notNullFieldName = "effectiveDays", message = "领取后有效天数不能为空"),
})
@Data
public class GoodsCouponEditParam implements Serializable {
    @ApiModelProperty(value = "优惠券值")
    @DecimalMin(value = "0.01", message = "优惠券值不能小于0.01")
    private BigDecimal couponValue;

    @ApiModelProperty(value = "门槛金额(0表示无限制)", required = true)
    @NotNull(message = "门槛金额不能为空")
    @DecimalMin(value = "0", message = "门槛金额不能小于0")
    private BigDecimal targetPrice;

    @ApiModelProperty(value = "商品自编码")
    private String goodsCode;

    @ApiModelProperty(value = "有效类型(coupon.EffectiveTypeEnum)")
    private EffectiveTypeEnum effectiveType;

    @ApiModelProperty(value = "领取后有效天数")
    private Integer effectiveDays;

    @ApiModelProperty(value = "有效开始时间")
    private Date effectiveStartTime;

    @ApiModelProperty(value = "有效结束时间")
    private Date effectiveEndTime;

    @ApiModelProperty(value = "有效核销开始时间(HH:mm:ss)")
    @JSONField(format = "HH:mm:ss")
    private Date verificateStartTime;

    @ApiModelProperty(value = "有效核销结束时间(HH:mm:ss)")
    @JSONField(format = "HH:mm:ss")
    private Date verificateEndTime;

    @ApiModelProperty(value = "优惠券说明")
    private String couponRemark;
}


