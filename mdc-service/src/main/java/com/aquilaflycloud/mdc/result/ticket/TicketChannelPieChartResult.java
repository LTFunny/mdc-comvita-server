package com.aquilaflycloud.mdc.result.ticket;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TicketChannelPieChartResult implements Serializable {
    private static final long serialVersionUID = 140599934741839253L;

    @ApiModelProperty(value = "渠道id")
    private Long id;

    @ApiModelProperty(value = "渠道名称")
    private String name;

    @ApiModelProperty(value = "渠道对应的数值")
    private Object value;

    public TicketChannelPieChartResult(Long id, String name, Object value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public TicketChannelPieChartResult() {
    }
}
