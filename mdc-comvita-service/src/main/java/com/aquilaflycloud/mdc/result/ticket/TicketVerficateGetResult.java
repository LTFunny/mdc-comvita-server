package com.aquilaflycloud.mdc.result.ticket;

import com.aquilaflycloud.mdc.model.ticket.TicketVerificateInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author zhi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketVerficateGetResult extends TicketVerificateResult implements Serializable {

    private Integer surplusVerificateNum;

    private BigDecimal verificatedAmount;
}
