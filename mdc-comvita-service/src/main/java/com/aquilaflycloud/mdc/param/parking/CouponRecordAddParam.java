package com.aquilaflycloud.mdc.param.parking;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.parking.CouponTypeEnum;
import com.aquilaflycloud.mdc.enums.parking.EffectiveTypeEnum;
import com.aquilaflycloud.mdc.enums.parking.InventoryTypeEnum;
import com.aquilaflycloud.mdc.enums.parking.LimitTypeEnum;
import com.aquilaflycloud.mdc.result.parking.CouponLimit;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * CouponRecordAddParam
 *
 * @author star
 * @date 2020-01-13
 */
@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "effectiveType", fieldValue = "CUSTOM", notNullFieldName = "effectiveStartTime", message = "有效开始时间不能为空"),
        @AnotherFieldHasValue(fieldName = "effectiveType", fieldValue = "CUSTOM", notNullFieldName = "effectiveEndTime", message = "有效结束时间不能为空"),
        @AnotherFieldHasValue(fieldName = "receiveLimitType", fieldValue = "CUSTOM", notNullFieldName = "receiveLimitContent", message = "领取限制内容不能为空"),
        @AnotherFieldHasValue(fieldName = "useLimitType", fieldValue = "CUSTOM", notNullFieldName = "useLimitContent", message = "使用限制内容不能为空"),
})
@Data
@Accessors(chain = true)
public class CouponRecordAddParam {
    @ApiModelProperty(value = "停车券名称", required = true)
    @NotBlank(message = "停车券名称不能为空")
    private String couponName;

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

    @ApiModelProperty(value = "库存类型(parking.InventoryTypeEnum)", required = true)
    @NotNull(message = "库存类型不能为空")
    private InventoryTypeEnum inventoryType;

    @ApiModelProperty(value = "库存值", required = true)
    @DecimalMin(value = "0.01", message = "库存值不能小于0")
    @NotNull(message = "库存值不能为空")
    private BigDecimal inventoryIncrease;

    @ApiModelProperty(value = "领取限制类型(parking.LimitTypeEnum)", required = true)
    @NotNull(message = "领取限制类型不能为空")
    private LimitTypeEnum receiveLimitType;

    @ApiModelProperty(value = "领取限制内容")
    @Valid
    private CouponLimit receiveLimitContent;

    @ApiModelProperty(value = "使用限制类型(parking.LimitTypeEnum)", required = true)
    @NotNull(message = "领取限制类型不能为空")
    private LimitTypeEnum useLimitType;

    @ApiModelProperty(value = "使用限制内容")
    @Valid
    private CouponLimit useLimitContent;

    @ApiModelProperty(value = "停车券所属部门ids(多个以,分隔)")
    private String designateOrgIds;
}
