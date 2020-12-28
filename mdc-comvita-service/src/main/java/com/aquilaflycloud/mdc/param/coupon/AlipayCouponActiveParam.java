package com.aquilaflycloud.mdc.param.coupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * AlipayCouponActiveParam
 *
 * @author star
 * @date 2020-06-30
 */
@Data
public class AlipayCouponActiveParam implements Serializable {
    @ApiModelProperty(value = "微信或支付宝appId", required = true)
    @NotBlank(message = "appId不能为空")
    private String appId;

    @ApiModelProperty(value = "优惠券id", required = true)
    @NotNull(message = "优惠券id不能为空")
    private Long couponId;


    @ApiModelProperty(value = "出资人登录账号", required = true)
    @NotBlank(message = "出资人登录账号不能为空")
    private String fundAccount;
}


