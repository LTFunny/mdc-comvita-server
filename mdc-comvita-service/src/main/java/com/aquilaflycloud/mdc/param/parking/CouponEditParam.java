package com.aquilaflycloud.mdc.param.parking;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.parking.EffectiveTypeEnum;
import com.aquilaflycloud.mdc.enums.parking.LimitTypeEnum;
import com.aquilaflycloud.mdc.result.parking.CouponLimit;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * CouponEditParam
 *
 * @author star
 * @date 2020-01-14
 */
@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "effectiveType", fieldValue = "CUSTOM", notNullFieldName = "effectiveStartTime", message = "有效开始时间不能为空"),
        @AnotherFieldHasValue(fieldName = "effectiveType", fieldValue = "CUSTOM", notNullFieldName = "effectiveEndTime", message = "有效结束时间不能为空"),
        @AnotherFieldHasValue(fieldName = "receiveLimitType", fieldValue = "CUSTOM", notNullFieldName = "receiveLimitContent", message = "领取限制内容不能为空"),
        @AnotherFieldHasValue(fieldName = "useLimitType", fieldValue = "CUSTOM", notNullFieldName = "useLimitContent", message = "使用限制内容不能为空"),
})
@Data
@Accessors(chain = true)
public class CouponEditParam {
    @ApiModelProperty(value = "停车券id", required = true)
    @NotNull(message = "停车券id不能为空")
    private Long id;

    @ApiModelProperty(value = "停车券名称")
    private String couponName;

    @ApiModelProperty(value = "优惠券使用说明")
    private String couponRemark;

    @ApiModelProperty(value = "有效类型(parking.EffectiveTypeEnum)")
    private EffectiveTypeEnum effectiveType;

    @ApiModelProperty(value = "有效开始时间")
    private Date effectiveStartTime;

    @ApiModelProperty(value = "有效结束时间")
    private Date effectiveEndTime;

    @ApiModelProperty(value = "领取限制类型(parking.LimitTypeEnum)")
    private LimitTypeEnum receiveLimitType;

    @ApiModelProperty(value = "领取限制内容")
    private CouponLimit receiveLimitContent;

    @ApiModelProperty(value = "使用限制类型(parking.LimitTypeEnum)")
    private LimitTypeEnum useLimitType;

    @ApiModelProperty(value = "使用限制内容")
    private CouponLimit useLimitContent;

    @ApiModelProperty(value = "停车券所属部门ids(多个以,分隔)")
    private String designateOrgIds;
}
