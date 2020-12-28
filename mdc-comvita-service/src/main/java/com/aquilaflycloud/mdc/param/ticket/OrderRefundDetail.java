package com.aquilaflycloud.mdc.param.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 订单退款详情
 */
@Data
@Accessors(chain = true)
public class OrderRefundDetail implements Serializable {

    private static final long serialVersionUID = -3797225691151278367L;

    @ApiModelProperty(value = "凭证码", required = true)
    @NotNull(message = "凭证码不能为空")
    private String eCode;

    @ApiModelProperty(value = "订单数量", required = true)
    @NotBlank(message = "订单数量不能为空")
    private Integer productCount;
}
