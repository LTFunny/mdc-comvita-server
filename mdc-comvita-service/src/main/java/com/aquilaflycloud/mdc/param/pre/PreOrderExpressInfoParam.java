package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author zengqingjie
 * @Date 2021-01-04
 */
@Data
public class PreOrderExpressInfoParam {
    @ApiModelProperty(value = "快递单号" , required = true)
    @NotNull(message = "快递单号不能为空")
    private String expressOrder;

    @ApiModelProperty(value = "快递名称" , required = true)
    @NotNull(message = "快递名称不能为空")
    private String expressName;

    @ApiModelProperty(value = "买家手机号" , required = true)
    @NotNull(message = "买家手机号不能为空")
    private String buyerPhone;
}
