package com.aquilaflycloud.mdc.param.exchange;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * OrderGetParam
 *
 * @author star
 * @date 2020-03-16
 */
@Data
public class OrderGetParam implements Serializable {
    @ApiModelProperty(value = "订单id", required = true)
    @NotNull(message = "订单id不能为空")
    private Long id;
}
