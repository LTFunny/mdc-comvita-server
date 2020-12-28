package com.aquilaflycloud.mdc.param.coupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * CouponRelUseParam
 *
 * @author star
 * @date 2020-03-10
 */
@Data
public class CouponRelUseParam implements Serializable {
    @ApiModelProperty(value = "核销码", required = true)
    @NotBlank(message = "核销码不能为空")
    private String verificateCode;

    @ApiModelProperty(value = "消费金额")
    private BigDecimal consumePrice;
}
