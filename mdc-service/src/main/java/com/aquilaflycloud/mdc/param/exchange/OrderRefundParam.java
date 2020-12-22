package com.aquilaflycloud.mdc.param.exchange;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * OrderRefundParam
 *
 * @author star
 * @date 2020-03-25
 */
@Data
public class OrderRefundParam implements Serializable {
    @ApiModelProperty(value = "订单id", required = true)
    @NotNull(message = "订单id不能为空")
    private Long id;

    @ApiModelProperty(value = "退款原因类型", required = true)
    @NotBlank(message = "退款原因类型不能为空")
    private String refundReasonType;

    @ApiModelProperty(value = "退款原因", required = true)
    @NotBlank(message = "退款原因不能为空")
    private String refundReason;
}
