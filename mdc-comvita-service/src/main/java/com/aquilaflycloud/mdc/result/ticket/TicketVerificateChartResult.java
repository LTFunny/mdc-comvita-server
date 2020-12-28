package com.aquilaflycloud.mdc.result.ticket;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author zhi
 */
@Data
public class TicketVerificateChartResult implements Serializable {

    private String date;

    private Integer verificateNum;
}
