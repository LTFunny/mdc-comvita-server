package com.aquilaflycloud.mdc.result.ticket;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TicketProductPieChartResult implements Serializable {
    private static final long serialVersionUID = 140599934741839253L;

    @ApiModelProperty(value = "门票类型id")
    private int id;

    @ApiModelProperty(value = "门票名称")
    private String name;

    @ApiModelProperty(value = "门票对应的数值")
    private Object value;

    public TicketProductPieChartResult(int id, String name, Object value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public TicketProductPieChartResult() {
    }
}
