package com.aquilaflycloud.mdc.result.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 产品售卖分析-顶部综合统计
 */
@Data
public class TicketProductSaleHeaderResult implements Serializable {
    private static final long serialVersionUID = 7409174779363340072L;
    @ApiModelProperty(value = "渠道销售金额")
    private BigDecimal amount;
    //渠道订单数
    @ApiModelProperty(value = "渠道订单数")
    private Long orderCount;
    //购票人数
    @ApiModelProperty(value = "购票人数")
    private Long personNumber;
    //人均消费金额
    @ApiModelProperty(value = "人均消费金额")
    private Double average;

    public TicketProductSaleHeaderResult(BigDecimal amount, Long orderCount, Long personNumber, Double average) {
        this.amount = amount;
        this.orderCount = orderCount;
        this.personNumber = personNumber;
        this.average = average;
    }

    public TicketProductSaleHeaderResult() {
    }
}
