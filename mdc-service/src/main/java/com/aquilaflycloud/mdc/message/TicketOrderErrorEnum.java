package com.aquilaflycloud.mdc.message;

import com.gitee.sop.servercommon.message.ServiceErrorMeta;

/**
 * TicketOrderErrorEnum
 *
 * @author zengqingjie
 * @date 2019-12-10
 */
public enum TicketOrderErrorEnum {
    /**
     * 有多张订单明细
     */
    TICKET_ORDER_ERROR_10301("10301"),
    TICKET_ORDER_ERROR_10302("10302"),
    TICKET_ORDER_ERROR_10303("10303"),
    TICKET_ORDER_ERROR_10304("10304"),
    TICKET_ORDER_ERROR_10305("10305"),
    TICKET_ORDER_ERROR_10306("10306"),
    TICKET_ORDER_ERROR_10307("10307"),
    TICKET_ORDER_ERROR_10308("10308"),
    TICKET_ORDER_ERROR_10309("10309"),
    TICKET_ORDER_ERROR_103010("103010"),
    TICKET_ORDER_ERROR_103011("103011"),
    ;
    private ServiceErrorMeta errorMeta;

    TicketOrderErrorEnum(String subCode) {
        this.errorMeta = new ServiceErrorMeta("mdc.ticket_order_error_", subCode);
    }

    public ServiceErrorMeta getErrorMeta() {
        return errorMeta;
    }
}
