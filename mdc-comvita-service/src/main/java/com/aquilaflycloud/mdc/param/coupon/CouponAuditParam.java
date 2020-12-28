package com.aquilaflycloud.mdc.param.coupon;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * CouponAuditParam
 *
 * @author star
 * @date 2020-03-10
 */
@AnotherFieldHasValue(fieldName = "isApprove", fieldValue = "false", notNullFieldName = "auditRemark", message = "审核详情不能为空")
@Data
@Accessors(chain = true)
public class CouponAuditParam implements Serializable {
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "优惠券id不能为空")
    private Long id;

    @ApiModelProperty(value = "是否审核通过", required = true)
    @NotNull(message = "isApprove不能为空")
    private Boolean isApprove;

    @ApiModelProperty(value = "审核详情")
    private String auditRemark;
}
