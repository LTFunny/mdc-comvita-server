package com.aquilaflycloud.mdc.param.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 释放订单入参
 */
@Data
@Accessors(chain = true)
public class OrderInfoWechatReleaseParam implements Serializable {
    private static final long serialVersionUID = 3191273016710307199L;
    @ApiModelProperty(value = "订单id", required = true)
    @NotNull(message = "订单id不能为空")
    private Long id;

    @ApiModelProperty(value = "ota订单号", required = true)
    @NotBlank(message = "ota订单号不能为空")
    private String OTAOrderNo;

}
