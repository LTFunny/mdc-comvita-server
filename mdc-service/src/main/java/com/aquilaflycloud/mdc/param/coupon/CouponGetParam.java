package com.aquilaflycloud.mdc.param.coupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * CouponGetParam
 *
 * @author star
 * @date 2020-03-09
 */
@Data
@Accessors(chain = true)
public class CouponGetParam implements Serializable {
    @ApiModelProperty(value = "优惠券id", required = true)
    @NotNull(message = "优惠券id不能为空")
    private Long id;
}
