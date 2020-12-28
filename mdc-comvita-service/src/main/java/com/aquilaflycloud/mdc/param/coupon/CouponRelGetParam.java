package com.aquilaflycloud.mdc.param.coupon;

import com.aquilaflycloud.common.NoneBlank;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * CouponRelGetParam
 *
 * @author star
 * @date 2020-03-10
 */
@NoneBlank(fieldNames = {"id", "verificateCode"}, message = "记录id和核销码不能同时为空")
@Data
public class CouponRelGetParam implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "核销码")
    private String verificateCode;
}
