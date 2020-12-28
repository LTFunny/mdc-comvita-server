package com.aquilaflycloud.mdc.param.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * OrderGetParam
 *
 * @author star
 * @date 2020-02-10
 */
@Data
@Accessors(chain = true)
public class OrderGetParam {
    @ApiModelProperty(value = "订单id", required = true)
    @NotNull(message = "订单id不能为空")
    private Long id;
}
