package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author pengyongliang
 * @Date 2020/12/30 11:13
 * @Version 1.0
 */
@Data
public class PreOrderInfoGetParam {

    @ApiModelProperty(value = "订单id" , required = true)
    @NotNull(message = "订单id不能为空")
    private Long id;
}
