package com.aquilaflycloud.mdc.result.ticket;

import com.aquilaflycloud.mdc.model.ticket.TicketOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author zhi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketVerificateOrderResult extends TicketOrderInfo implements Serializable {

    @ApiModelProperty(value = "剩余核销次数")
    private Integer surplusVerificateNum;
}
