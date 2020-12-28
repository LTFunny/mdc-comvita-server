package com.aquilaflycloud.mdc.result.exchange;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * OrderStatisticsResult
 *
 * @author star
 * @date 2020-04-07
 */
@Data
public class OrderStatisticsResult {
    @ApiModelProperty(value = "订单总数")
    private Integer orderCount;

    @ApiModelProperty(value = "待支付总数")
    private Integer notPayCount;

    @ApiModelProperty(value = "已支付总数")
    private Integer paidCount;

    @ApiModelProperty(value = "待收货总数")
    private Integer deliveringCount;

    @ApiModelProperty(value = "已完成总数")
    private Integer successCount;

    @ApiModelProperty(value = "退款总数")
    private Integer refundCount;

    @ApiModelProperty(value = "成交订单数")
    private Integer dealCount;

    @ApiModelProperty(value = "成交订单转换率")
    private String dealRate;

    @ApiModelProperty(value = "成交金额")
    private BigDecimal dealPrice;

    @ApiModelProperty(value = "成交人数")
    private Integer dealMemberCount;

    @ApiModelProperty(value = "客单价")
    private BigDecimal dealMemberPrice;
}
