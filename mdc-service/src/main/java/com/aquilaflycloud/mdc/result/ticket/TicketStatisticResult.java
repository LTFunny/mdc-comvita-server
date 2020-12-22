package com.aquilaflycloud.mdc.result.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author zhi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketStatisticResult extends TicketVerificateStatisticResult {

    @ApiModelProperty(value = "应核销次数")
    private Long canUsedNum;

    @ApiModelProperty(value = "未核销次数")
    private Long unusedNum;
}
