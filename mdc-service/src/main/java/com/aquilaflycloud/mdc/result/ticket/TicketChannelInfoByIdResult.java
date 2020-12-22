package com.aquilaflycloud.mdc.result.ticket;

import com.aquilaflycloud.mdc.model.ticket.TicketChannelInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author zhi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketChannelInfoByIdResult extends TicketChannelInfo implements Serializable {
    @ApiModelProperty(value = "推荐标题")
    private String recommendTitle;
}
