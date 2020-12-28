package com.aquilaflycloud.mdc.result.ticket;


import com.aquilaflycloud.mdc.model.ticket.TicketOrderProductInfo;
import com.aquilaflycloud.mdc.model.ticket.TicketOrderProductInfoDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TicketWechatProductInfoDetailResult implements Serializable {

    private static final long serialVersionUID = -7572818558333212452L;

    @ApiModelProperty(value = "产品信息")
    private TicketOrderProductInfo ticketProductInfo;

    @ApiModelProperty(value = "产品详情")
    private List<TicketOrderProductInfoDetail> ticketProductInfoDetailList;

    public TicketWechatProductInfoDetailResult(TicketOrderProductInfo ticketProductInfo, List<TicketOrderProductInfoDetail> ticketProductInfoDetailList) {
        this.ticketProductInfo = ticketProductInfo;
        this.ticketProductInfoDetailList = ticketProductInfoDetailList;
    }

    public TicketWechatProductInfoDetailResult() {
    }
}
