package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.ticket.ScenicSpotTypeEnum;
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
public class OrderRefundPageParam extends PageAuthParam implements Serializable {

    @ApiModelProperty(value = "景区类型(0海洋馆;1博物馆;2雨林馆)")
    private ScenicSpotTypeEnum scenicSpotType;

    @ApiModelProperty(value = "订单号关键字")
    private String outOrderNoLike;

    @ApiModelProperty(value = "手机号关键字")
    private String mobileLike;

    @ApiModelProperty(value = "产品名称关键字")
    private String productNameLike;

    @ApiModelProperty(value = "下单时间起始")
    private Date createTimeStart;

    @ApiModelProperty(value = "下单时间结束")
    private Date createTimeEnd;

    @ApiModelProperty(value = "退票时间起始")
    private Date refundTimeStart;

    @ApiModelProperty(value = "退票时间结束")
    private Date refundTimeEnd;

    @ApiModelProperty(value = "最小订单金额")
    private BigDecimal minAmount;

    @ApiModelProperty(value = "最大订单金额")
    private BigDecimal maxAmount;
}
