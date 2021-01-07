package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单报表
 * <p>
 * zly
 */
@Accessors(chain = true)
@Data
public class ReportOrderPageResult {

    @ApiModelProperty(value = "门店名称")
    private String shopName;

    @ApiModelProperty(value = "订单数量")
    private String orderNumber;

    @ApiModelProperty(value = "订单总金额")
    private String orderTotalPrice;

    @ApiModelProperty(value = "退货订单数量")
    private String refundOrderNumber;

    @ApiModelProperty(value = "退货订单总金额")
    private String refundOrderTotalPrice;

}
