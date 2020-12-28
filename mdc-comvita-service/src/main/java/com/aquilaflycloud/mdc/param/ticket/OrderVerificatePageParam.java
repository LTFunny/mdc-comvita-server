package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.ticket.OrderInfoStatusEnum;
import com.aquilaflycloud.mdc.enums.ticket.ScenicSpotTypeEnum;
import com.aquilaflycloud.mdc.model.ticket.TicketOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author zhi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderVerificatePageParam extends PageAuthParam<TicketOrderInfo> implements Serializable {

    @ApiModelProperty(value = "景区类型(ticket.ScenicSpotTypeEnum)")
    private ScenicSpotTypeEnum type;

    @ApiModelProperty(value = "订单号关键字")
    private String outOrderNo;

    @ApiModelProperty(value = "手机号关键字")
    private String mobile;

    @ApiModelProperty(value = "产品名称关键字")
    private String productName;

    @ApiModelProperty(value = "下单时间起始")
    private Date createTimeStart;

    @ApiModelProperty(value = "下单时间结束")
    private Date createTimeEnd;

    @ApiModelProperty(value = "游玩开始时间")
    private Date startDate;

    @ApiModelProperty(value = "游玩结束时间")
    private Date endDate;

    @ApiModelProperty(value = "最小订单金额")
    private BigDecimal minAmount;

    @ApiModelProperty(value = "最大订单金额")
    private BigDecimal maxAmount;

    @ApiModelProperty(value = "订单状态(ticket.OrderInfoStatusEnum)")
    private OrderInfoStatusEnum status;
}
