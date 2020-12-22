package com.aquilaflycloud.mdc.param.coupon;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.coupon.CouponTypeEnum;
import com.aquilaflycloud.mdc.enums.coupon.DisableTypeEnum;
import com.aquilaflycloud.mdc.enums.coupon.EffectiveTypeEnum;
import com.aquilaflycloud.mdc.enums.coupon.ReleaseTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * AlipayCouponAddParam
 *
 * @author star
 * @date 2020-06-29
 */
@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "couponType", fieldValue = "ALIPAYCASHVOUCHER", canNullFieldName = "couponValue", message = "优惠券值不能为空"),
        @AnotherFieldHasValue(fieldName = "effectiveType", fieldValue = "DATERANGE", notNullFieldName = "effectiveStartTime", message = "有效开始时间不能为空"),
        @AnotherFieldHasValue(fieldName = "effectiveType", fieldValue = "DATERANGE", notNullFieldName = "effectiveEndTime", message = "有效结束时间不能为空"),
        @AnotherFieldHasValue(fieldName = "effectiveType", fieldValue = "AFTERDAYS", notNullFieldName = "effectiveDays", message = "领取后有效天数不能为空"),
        @AnotherFieldHasValue(fieldName = "releaseType", fieldValue = "REGULAR", notNullFieldName = "releaseTime", message = "发放时间不能为空"),
        @AnotherFieldHasValue(fieldName = "disableType", fieldValue = "REGULAR", notNullFieldName = "disableTime", message = "下架时间不能为空"),
})
@Data
public class AlipayCouponAddParam implements Serializable {
    @ApiModelProperty(value = "优惠券品牌名称", required = true)
    @NotBlank(message = "优惠券品牌名称不能为空")
    private String couponName;

    @ApiModelProperty(value = "优惠券图片")
    private String couponImg;

    @ApiModelProperty(value = "优惠券类型(coupon.CouponTypeEnum)", required = true)
    @NotNull(message = "优惠券类型不能为空")
    private CouponTypeEnum couponType;

    @ApiModelProperty(value = "优惠券值")
    @DecimalMin(value = "0.01", message = "优惠券值不能小于0.01")
    private BigDecimal couponValue;

    @ApiModelProperty(value = "门槛金额", required = true)
    @NotNull(message = "门槛金额不能为空")
    @DecimalMin(value = "0.01", message = "门槛金额不能小于0.01")
    private BigDecimal targetPrice;

    @ApiModelProperty(value = "优惠券库存", required = true)
    @NotNull(message = "优惠券库存不能为空")
    @Min(value = 1, message = "库存不能小于1")
    private Integer inventory;

    @ApiModelProperty(value = "会员领取上限", required = true)
    @NotNull(message = "上限不能为空")
    @Min(value = 1, message = "上限不能小于1")
    private Integer receiveLimit;

    @ApiModelProperty(value = "是否显示(默认不显示)(common.WhetherEnum)")
    private WhetherEnum canShow = WhetherEnum.NO;

    @ApiModelProperty(value = "可领取开始时间", required = true)
    @NotNull(message = "可领取开始时间不能为空")
    private Date receiveStartTime;

    @ApiModelProperty(value = "可领取结束时间", required = true)
    @NotNull(message = "可领取结束时间不能为空")
    private Date receiveEndTime;

    @ApiModelProperty(value = "有效类型(coupon.EffectiveTypeEnum)", required = true)
    @NotNull(message = "有效类型不能为空")
    private EffectiveTypeEnum effectiveType;

    @ApiModelProperty(value = "领取后有效天数")
    private Integer effectiveDays;

    @ApiModelProperty(value = "有效开始时间")
    private Date effectiveStartTime;

    @ApiModelProperty(value = "有效结束时间")
    private Date effectiveEndTime;

    @ApiModelProperty(value = "优惠券说明(最多10条)")
    @Size(max = 10, message = "优惠券说明超过限制")
    private List<String> couponRemarkList;

    @ApiModelProperty(value = "发放类型(默认立即发放)(coupon.ReleaseTypeEnum)")
    private ReleaseTypeEnum releaseType = ReleaseTypeEnum.IMMEDIATELY;

    @ApiModelProperty(value = "发放时间")
    private Date releaseTime;

    @ApiModelProperty(value = "下架类型(默认手动下架)(coupon.DisableTypeEnum)")
    private DisableTypeEnum disableType = DisableTypeEnum.MANUAL;

    @ApiModelProperty(value = "下架时间")
    private Date disableTime;

    @ApiModelProperty(value = "商户ids(多个以,分隔)")
    private String designateOrgIds;

    @ApiModelProperty(value = "分类id列表", required = true)
    @NotEmpty(message = "分类id列表不能为空")
    private List<Long> catalogIdList;
}


