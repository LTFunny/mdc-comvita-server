package com.aquilaflycloud.mdc.result.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 渠道销售概况-顶部综合统计
 */
@Data
public class TicketChannelSaleHeaderResult implements Serializable {
    private static final long serialVersionUID = 7409174779363340072L;
    @ApiModelProperty(value = "渠道销售金额")
    private BigDecimal channelAmount;
    //渠道订单数
    @ApiModelProperty(value = "渠道订单数")
    private Long channelOrderCount;
    //购票人数
    @ApiModelProperty(value = "购票人数")
    private Long channelPersonNumber;
    //人均消费金额
    @ApiModelProperty(value = "人均消费金额")
    private Double channelAverage;

    public TicketChannelSaleHeaderResult(BigDecimal channelAmount, Long channelOrderCount, Long channelPersonNumber, Double channelAverage) {
        this.channelAmount = channelAmount;
        this.channelOrderCount = channelOrderCount;
        this.channelPersonNumber = channelPersonNumber;
        this.channelAverage = channelAverage;
    }

    public TicketChannelSaleHeaderResult() {
    }
}
