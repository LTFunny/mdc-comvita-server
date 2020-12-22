package com.aquilaflycloud.mdc.result.ticket;

import com.aquilaflycloud.mdc.model.ticket.TicketOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author zhi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketOrderResult extends TicketOrderInfo implements Serializable {

    /**
     * 微信昵称
     */
    @ApiModelProperty(value = "微信昵称")
    private String wxNickName;

    @ApiModelProperty(value = "购买渠道")
    private String channel;

    @ApiModelProperty(value="退票金额")
    private BigDecimal refundAmount;
}
