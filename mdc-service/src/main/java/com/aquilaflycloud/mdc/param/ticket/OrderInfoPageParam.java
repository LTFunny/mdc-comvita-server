package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.ticket.OrderInfoStatusEnum;
import com.aquilaflycloud.mdc.enums.ticket.ScenicSpotTypeEnum;
import com.aquilaflycloud.mdc.model.ticket.TicketOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class OrderInfoPageParam extends PageAuthParam<TicketOrderInfo> implements Serializable {
    private static final long serialVersionUID = 5045768429747134502L;

    @ApiModelProperty(value = "景区类型(ticket.ScenicSpotTypeEnum)0海洋馆1博物馆2雨林馆")
    private ScenicSpotTypeEnum type;

    @ApiModelProperty(value = "订单号")
    private String outOrderNo;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "产品名称")
    private String productName;

    @ApiModelProperty(value = "道控产品ID")
    private Integer productId;

    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @ApiModelProperty(value = "订单状态(ticket.OrderInfoStatusEnum)")
    private OrderInfoStatusEnum status;

    @ApiModelProperty(value = "订单最小金额")
    private BigDecimal smallAmount;

    @ApiModelProperty(value = "订单最大金额")
    private BigDecimal bigAmount;

    @ApiModelProperty(value = "开始游玩时间")
    private Date startPlayDate;

    @ApiModelProperty(value = "结束游玩时间")
    private Date endPlayDate;
}
