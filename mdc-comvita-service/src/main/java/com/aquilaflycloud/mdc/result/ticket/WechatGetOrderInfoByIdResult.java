package com.aquilaflycloud.mdc.result.ticket;

import com.aquilaflycloud.mdc.model.ticket.TicketOrderCustomerInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WechatGetOrderInfoByIdResult implements Serializable {
    //产品信息详情
    @ApiModelProperty(value = "产品信息详情")
    private TicketWechatProductInfoDetailResult ticketProductInfoDetailResult;
    //出行人相关信息
    @ApiModelProperty(value = "出行人相关信息")
    private List<TicketOrderCustomerInfo> ticketOrderCustomerInfos;
    //订单相关信息
    @ApiModelProperty(value = "订单相关信息")
    private WechatTicketOrderInfoByIdResult ticketOrderInfo;

    public WechatGetOrderInfoByIdResult() {
    }

    public WechatGetOrderInfoByIdResult(TicketWechatProductInfoDetailResult ticketProductInfoDetailResult, List<TicketOrderCustomerInfo> ticketOrderCustomerInfos, WechatTicketOrderInfoByIdResult ticketOrderInfo) {
        this.ticketProductInfoDetailResult = ticketProductInfoDetailResult;
        this.ticketOrderCustomerInfos = ticketOrderCustomerInfos;
        this.ticketOrderInfo = ticketOrderInfo;
    }
}
