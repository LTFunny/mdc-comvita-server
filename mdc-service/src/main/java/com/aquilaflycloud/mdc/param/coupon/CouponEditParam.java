package com.aquilaflycloud.mdc.param.coupon;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.coupon.DisableTypeEnum;
import com.aquilaflycloud.mdc.enums.coupon.EffectiveTypeEnum;
import com.aquilaflycloud.mdc.enums.coupon.ReceiveTypeEnum;
import com.aquilaflycloud.mdc.enums.coupon.ReleaseTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * CouponEditParam
 *
 * @author star
 * @date 2020-03-09
 */
@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "receiveType", fieldValue = "CUSTOM", notNullFieldName = "receiveStartTime", message = "可领取开始时间不能为空"),
        @AnotherFieldHasValue(fieldName = "receiveType", fieldValue = "CUSTOM", notNullFieldName = "receiveEndTime", message = "可领取结束时间不能为空"),
        @AnotherFieldHasValue(fieldName = "effectiveType", fieldValue = "DATERANGE", notNullFieldName = "effectiveStartTime", message = "有效开始时间不能为空"),
        @AnotherFieldHasValue(fieldName = "effectiveType", fieldValue = "DATERANGE", notNullFieldName = "effectiveEndTime", message = "有效结束时间不能为空"),
        @AnotherFieldHasValue(fieldName = "effectiveType", fieldValue = "AFTERDAYS", notNullFieldName = "effectiveDays", message = "领取后有效天数不能为空"),
        @AnotherFieldHasValue(fieldName = "releaseType", fieldValue = "REGULAR", notNullFieldName = "releaseTime", message = "发放时间不能为空"),
        @AnotherFieldHasValue(fieldName = "disableType", fieldValue = "REGULAR", notNullFieldName = "disableTime", message = "下架时间不能为空"),
})
@Data
public class CouponEditParam implements Serializable {
    @ApiModelProperty(value = "优惠券id", required = true)
    @NotNull(message = "优惠券id不能为空")
    private Long id;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "优惠券图片")
    private String couponImg;

    @ApiModelProperty(value = "优惠券值")
    @DecimalMin(value = "0.01", message = "优惠券值不能小于0.01")
    private BigDecimal couponValue;

    @ApiModelProperty(value = "门槛金额(0表示无限制)")
    @DecimalMin(value = "0", message = "门槛金额不能小于0")
    private BigDecimal targetPrice;

    @ApiModelProperty(value = "商品自编码")
    private String goodsCode;

    @ApiModelProperty(value = "优惠券成本")
    @DecimalMin(value = "0", message = "优惠券成本不能小于0")
    private BigDecimal costPrice;

    @ApiModelProperty(value = "优惠券增加库存")
    @Min(value = 0, message = "库存不能小于0")
    private Integer inventoryIncrease;

    @ApiModelProperty(value = "会员领取上限")
    @Min(value = 1, message = "上限不能小于1")
    private Integer receiveLimit;

    @ApiModelProperty(value = "是否显示(common.WhetherEnum)")
    private WhetherEnum canShow;

    @ApiModelProperty(value = "领取类型(coupon.ReceiveTypeEnum)")
    private ReceiveTypeEnum receiveType;

    @ApiModelProperty(value = "可领取开始时间")
    private Date receiveStartTime;

    @ApiModelProperty(value = "可领取结束时间")
    private Date receiveEndTime;

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

    @ApiModelProperty(value = "发放类型(coupon.ReleaseTypeEnum)")
    private ReleaseTypeEnum releaseType;

    @ApiModelProperty(value = "发放时间")
    private Date releaseTime;

    @ApiModelProperty(value = "下架类型(coupon.DisableTypeEnum)")
    private DisableTypeEnum disableType;

    @ApiModelProperty(value = "下架时间")
    private Date disableTime;

    @ApiModelProperty(value = "商户ids(多个以,分隔)")
    private String designateOrgIds;

    @ApiModelProperty(value = "分类id列表")
    private List<Long> catalogIdList;
}
