package com.aquilaflycloud.mdc.param.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 退款入参
 */
@Data
@Accessors(chain = true)
public class OrderInfoWechatUpdateParam implements Serializable {

    private static final long serialVersionUID = 5240982857134530317L;

    @ApiModelProperty(value = "订单关系表id", required = true)
    @NotNull(message = "订单关系id不能为空")
    private Long id;

    @ApiModelProperty(value = "退款是否成功", required = true)
    @NotNull(message = "成功标识不能为空")
    private Boolean isSuccess;
}
