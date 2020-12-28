package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.mdc.model.ticket.TicketOrderCustomerInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单顾客信息
 */
@Data
@Accessors(chain = true)
public class OrderInfoDetailInfo implements Serializable {
    @ApiModelProperty(value = "产品id", required = true)
    @NotNull(message = "产品id不能为空")
    private int productId;

    @ApiModelProperty(value = "订单数量", required = true)
    @NotNull(message = "订单数量不能为空")
    private int productCount;

    @ApiModelProperty(value = "结算价", required = true)
    @NotNull(message = "结算价不能为空")
    private BigDecimal productPrice;

    @ApiModelProperty(value = "售卖价", required = true)
    @NotNull(message = "售卖价不能为空")
    private BigDecimal productSellPrice;

    @ApiModelProperty(value = "出行人结果集")
    private List<OrderInfoCustomerInfo> customerInfos;

    @ApiModelProperty(value = "游客信息集合", required = true)
    @NotNull(message = "游客信息不能为空")
    private OrderInfoCustomerInfo customerInfo;
}
