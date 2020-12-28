package com.aquilaflycloud.mdc.result.ticket;

import com.aquilaflycloud.mdc.model.ticket.TicketOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WechatTicketOrderInfoByIdResult extends TicketOrderInfo {
    //产品信息详情
    @ApiModelProperty(value = "当前时间与创建时间间隔(秒)")
    private Long diff;
}
