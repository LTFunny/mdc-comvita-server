package com.aquilaflycloud.mdc.result.ticket;

import com.aquilaflycloud.mdc.model.ticket.TicketOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class TicketOrderInfoSalesResult extends TicketOrderInfo implements Serializable {
    private static final long serialVersionUID = 2081559238670862310L;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;
}
