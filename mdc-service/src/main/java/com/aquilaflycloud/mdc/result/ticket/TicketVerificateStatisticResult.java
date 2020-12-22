package com.aquilaflycloud.mdc.result.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author zhi
 */
@Data
public class TicketVerificateStatisticResult implements Serializable {

    @ApiModelProperty(value = "已核销次数")
    private Long usedNum;

    @ApiModelProperty(value = "已核销订单数")
    private Long verificateOrderNum;

}
