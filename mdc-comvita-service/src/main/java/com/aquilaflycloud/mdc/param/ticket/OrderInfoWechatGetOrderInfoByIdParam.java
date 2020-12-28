package com.aquilaflycloud.mdc.param.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 订单详情入参
 */
@Data
@Accessors(chain = true)
public class OrderInfoWechatGetOrderInfoByIdParam implements Serializable {
    private static final long serialVersionUID = -4470772065631269738L;

    @ApiModelProperty(value = "订单id", required = true)
    @NotNull(message = "订单id不能为空")
    private Long id;
}
