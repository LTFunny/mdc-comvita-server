package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @Author pengyongliang
 * @Date 2021/1/11 17:17
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class PreOrderGetParam {

    @ApiModelProperty(value = "订单id" , required = true)
    @NotNull(message = "订单id")
    private Long orderId;
}
