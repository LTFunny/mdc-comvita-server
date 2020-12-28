package com.aquilaflycloud.mdc.result.ticket;

import com.aquilaflycloud.mdc.model.ticket.TicketOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ProductInfoDetailResult
 *
 * @author Zengqingjie
 * @date 2019-10-29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketOrderInfoDetailResult extends TicketOrderInfo {
    @ApiModelProperty(value = "渠道名称")
    private String channelName;
}
