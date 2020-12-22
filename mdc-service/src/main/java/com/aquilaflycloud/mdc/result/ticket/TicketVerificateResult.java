package com.aquilaflycloud.mdc.result.ticket;

import com.aquilaflycloud.mdc.model.ticket.TicketOrderCustomerInfo;
import com.aquilaflycloud.mdc.model.ticket.TicketOrderInfo;
import com.aquilaflycloud.mdc.model.ticket.TicketVerificateInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author zhi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketVerificateResult extends TicketOrderInfo implements Serializable {

    @ApiModelProperty(value = "剩余核销次数")
    private Integer surplusVerificateTimes;

    private BigDecimal surplusVerificateAmount;

    /**
     * 微信昵称
     */
    @ApiModelProperty(value = "微信昵称")
    private String wxNickName;

    @ApiModelProperty(value = "购买渠道")
    private String channel;

    @ApiModelProperty(value = "支付单号")
    private Long orderId;

    @ApiModelProperty(value = "支付时间")
    private Date payTime;

    @ApiModelProperty(value = "退款订单号")
    private String refundOrderNo;

    @ApiModelProperty(value = "退款时间")
    private Date refundTime;

    @ApiModelProperty(value = "核销记录")
    private List<TicketVerificateInfo> verificateInfos;

    @ApiModelProperty(value = "出行人信息")
    private List<TicketOrderCustomerInfo> customers;
}
