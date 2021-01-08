package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author pengyongliang
 * @Date 2021/1/6 15:11
 * @Version 1.0
 */
@Data
public class PreOrderInfoGetParam {

    @ApiModelProperty(value = "订单id" , required = true)
    @NotNull(message = "订单id不能为空")
    private Long orderInfoId;

    @ApiModelProperty(value = "售后标识")
    private int after;
}
