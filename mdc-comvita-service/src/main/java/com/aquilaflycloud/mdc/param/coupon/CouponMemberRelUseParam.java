package com.aquilaflycloud.mdc.param.coupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * CouponMemberRelUseParam
 *
 * @author star
 * @date 2020-04-01
 */
@Data
public class CouponMemberRelUseParam implements Serializable {
    @ApiModelProperty(value = "核销码", required = true)
    @NotBlank(message = "核销码不能为空")
    private String verificateCode;

    @ApiModelProperty(value = "核销方部门id", required = true)
    @NotNull(message = "核销方部门id不能为空")
    private Long orgId;

    @ApiModelProperty(value = "消费金额")
    private BigDecimal consumePrice;
}
