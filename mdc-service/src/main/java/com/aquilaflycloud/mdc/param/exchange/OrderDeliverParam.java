package com.aquilaflycloud.mdc.param.exchange;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * OrderDeliverParam
 *
 * @author star
 * @date 2020-03-21
 */
@Data
public class OrderDeliverParam implements Serializable {
    @ApiModelProperty(value = "订单id", required = true)
    @NotNull(message = "订单id不能为空")
    private Long id;

    @ApiModelProperty(value = "快递名称", required = true)
    @NotBlank(message = "快递名称不能为空")
    private String expressName;

    @ApiModelProperty(value = "快递单号", required = true)
    @NotBlank(message = "快递单号不能为空")
    private String expressOrder;

}
