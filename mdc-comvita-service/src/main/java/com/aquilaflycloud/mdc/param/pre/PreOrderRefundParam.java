package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.mdc.enums.pre.AfterSaleTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * PreOrderRefundParam
 *
 * @author star
 * @date 2021/1/7
 */
@Data
public class PreOrderRefundParam {
    @ApiModelProperty(value = "订单id", required = true)
    @NotNull(message = "订单id不能为空")
    private Long orderId;

    @ApiModelProperty(value = "退款金额", required = true)
    @NotNull(message = "退款金额不能为空")
    private BigDecimal refundPrice;

    @ApiModelProperty(value = "商品状态(pre.AfterSaleTypeEnum)", required = true)
    @NotNull(message = "商品状态不能为空")
    private AfterSaleTypeEnum refundGoodsState;

    @ApiModelProperty(value = "退款原因")
    private String refundReason;
}
