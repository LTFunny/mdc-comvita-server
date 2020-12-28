package com.aquilaflycloud.mdc.param.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 付款微信支付参数入参
 */
@Data
@Accessors(chain = true)
public class OrderInfoWechatPrePayParam implements Serializable {
    private static final long serialVersionUID = 5082097420060661910L;
    @ApiModelProperty(value = "订单id", required = true)
    @NotNull(message = "订单id不能为空")
    private Long id;
}
