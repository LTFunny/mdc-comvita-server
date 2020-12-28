package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.mdc.enums.easypay.PaymentTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 订单支付入参
 */
@Data
@Accessors(chain = true)
public class OrderInfoWechatPayParam implements Serializable {
    private static final long serialVersionUID = -3079004528184371409L;
    @ApiModelProperty(value = "关系id", required = true)
    @NotNull(message = "关系id不能为空")
    private Long id;

    @ApiModelProperty(value = "支付方式", required = true)
    @NotBlank(message = "支付方式不能为空")
    private PaymentTypeEnum paymentType;

    @ApiModelProperty(value = "支付是否成功", required = true)
    @NotNull(message = "成功标识不能为空")
    private Boolean isSuccess;

}
