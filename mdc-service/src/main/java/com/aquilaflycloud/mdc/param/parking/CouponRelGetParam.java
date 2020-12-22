package com.aquilaflycloud.mdc.param.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * CouponRelGetParam
 *
 * @author star
 * @date 2020-02-10
 */
@Data
@Accessors(chain = true)
public class CouponRelGetParam {
    @ApiModelProperty(value = "会员停车券记录id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;
}
