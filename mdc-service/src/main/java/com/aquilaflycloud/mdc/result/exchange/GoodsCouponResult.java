package com.aquilaflycloud.mdc.result.exchange;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.coupon.CouponTypeEnum;
import com.aquilaflycloud.mdc.enums.coupon.EffectiveTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * GoodsCouponResult
 *
 * @author star
 * @date 2020-03-23
 */
@Data
public class GoodsCouponResult implements Serializable {
    @ApiModelProperty(value = "优惠券类型")
    private CouponTypeEnum couponType;

    @ApiModelProperty(value = "优惠券值")
    private BigDecimal couponValue;

    @ApiModelProperty(value = "门槛金额")
    private BigDecimal targetPrice;

    @ApiModelProperty(value = "商品自编码")
    private String goodsCode;

    @ApiModelProperty(value = "有效类型")
    private EffectiveTypeEnum effectiveType;

    @ApiModelProperty(value = "领取后有效天数")
    private Integer effectiveDays;

    @ApiModelProperty(value = "有效开始时间")
    private Date effectiveStartTime;

    @ApiModelProperty(value = "有效结束时间")
    private Date effectiveEndTime;

    @ApiModelProperty(value = "有效核销开始时间")
    @JSONField(format = "HH:mm:ss")
    private Date verificateStartTime;

    @ApiModelProperty(value = "有效核销结束时间")
    @JSONField(format = "HH:mm:ss")
    private Date verificateEndTime;

    @ApiModelProperty(value = "优惠券说明")
    private String couponRemark;
}


