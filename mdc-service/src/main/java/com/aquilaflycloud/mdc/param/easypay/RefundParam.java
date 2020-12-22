package com.aquilaflycloud.mdc.param.easypay;

import com.aquilaflycloud.mdc.enums.easypay.PayTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * RefundParam
 *
 * @author star
 * @date 2019-12-07
 */
@Data
@Accessors(chain = true)
public class RefundParam {
    @ApiModelProperty(value = "订单id", required = true)
    @NotNull(message = "订单id不能为空")
    private Long id;

    @ApiModelProperty(value = "appId")
    private String appId;

    @ApiModelProperty(value = "支付类型", required = true)
    @NotNull(message = "支付类型不能为空")
    private PayTypeEnum payType;

    @ApiModelProperty(value = "订单号", required = true)
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @ApiModelProperty(value = "退款订单号", required = true)
    @NotBlank(message = "退款订单号不能为空")
    private String refundOrderNo;

    @ApiModelProperty(value = "退款金额", required = true)
    @NotNull(message = "退款金额不能为空")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "退款原因描述")
    private String refundReason;

}


