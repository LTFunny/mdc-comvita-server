package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @Author pengyongliang
 * @Date 2020/12/30 11:13
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class PreOrderGoodsGetParam {

    @ApiModelProperty(value = "订单明细id" , required = true)
    @NotNull(message = "订单明细id不能为空")
    private Long orderGoodsId;
}
