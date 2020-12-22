package com.aquilaflycloud.mdc.result.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 产品售卖分析结果集
 */
@Data
public class TicketProductSaleResult implements Serializable {
    private static final long serialVersionUID = 4056879799892872689L;

    @ApiModelProperty(value = "顶部统计结果集")
    private TicketProductSaleHeaderResult ticketProductSaleHeaderResult;

    @ApiModelProperty(value = "折线图日期结果集")
    private List<String> days;

    @ApiModelProperty(value = "折线图订单数量结果集")
    private List<Long> orderCountList;

    @ApiModelProperty(value = "折线图销售金额结果集")
    private List<Double> sellAmountList;

    @ApiModelProperty(value = "饼状图销售金额结果集")
    private List<TicketProductPieChartResult> productOrderCountList;

    @ApiModelProperty(value = "饼状图销售金额结果集")
    private List<TicketProductPieChartResult> productSellAmountList;
}
