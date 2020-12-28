package com.aquilaflycloud.mdc.result.ticket;


import com.aquilaflycloud.mdc.model.ticket.TicketProductInfoDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TicketProductInfoDetailByIdResult implements Serializable {

    private static final long serialVersionUID = -7572818558333212452L;

    @ApiModelProperty(value = "产品信息")
    private ProductInfoByIdResult ticketProductInfo;

    @ApiModelProperty(value = "产品详情")
    private List<TicketProductInfoDetail> ticketProductInfoDetailList;

    public TicketProductInfoDetailByIdResult(ProductInfoByIdResult ticketProductInfo, List<TicketProductInfoDetail> ticketProductInfoDetailList) {
        this.ticketProductInfo = ticketProductInfo;
        this.ticketProductInfoDetailList = ticketProductInfoDetailList;
    }

    public TicketProductInfoDetailByIdResult() {
    }
}
