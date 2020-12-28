package com.aquilaflycloud.mdc.result.ticket;


import com.aquilaflycloud.mdc.model.easypay.EasypayPaymentRecord;
import com.aquilaflycloud.mdc.model.easypay.EasypayRefundRecord;
import com.aquilaflycloud.mdc.model.ticket.TicketOrderCustomerInfo;
import com.aquilaflycloud.mdc.model.ticket.TicketOrderProductInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TicketOrderInfoResult implements Serializable {

    private static final long serialVersionUID = -7572818558333212452L;

    @ApiModelProperty(value = "产品信息")
    private TicketOrderProductInfo ticketProductInfo;

    @ApiModelProperty(value = "订单信息")
    private TicketOrderInfoDetailResult ticketOrderInfo;

    @ApiModelProperty(value = "订单出行人结果集")
    private List<TicketOrderCustomerInfo> ticketOrderCustomerInfos;

    @ApiModelProperty(value = "支付记录")
    private EasypayPaymentRecord easypayPaymentRecord;

    @ApiModelProperty(value = "退款记录")
    private EasypayRefundRecord easypayRefundRecord;

    public TicketOrderInfoResult(TicketOrderProductInfo ticketProductInfo, TicketOrderInfoDetailResult ticketOrderInfo, List<TicketOrderCustomerInfo> ticketOrderCustomerInfos, EasypayPaymentRecord easypayPaymentRecord, EasypayRefundRecord easypayRefundRecord) {
        this.ticketProductInfo = ticketProductInfo;
        this.ticketOrderInfo = ticketOrderInfo;
        this.ticketOrderCustomerInfos = ticketOrderCustomerInfos;
        this.easypayPaymentRecord = easypayPaymentRecord;
        this.easypayRefundRecord = easypayRefundRecord;
    }

    public TicketOrderInfoResult() {
    }
}
