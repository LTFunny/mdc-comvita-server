package com.aquilaflycloud.mdc.extra.docom.notify;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 回调订单信息
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-27
 */
@Data
public class TicketCallbackOrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商户编码")
    private String MerchantCode;

    @ApiModelProperty(value = "商户名称")
    private String MerchantName;

    @ApiModelProperty(value = "商户订单号")
    private String OrderNo;

    @ApiModelProperty(value = "订单号")
    private String OutOrderNo;

    @ApiModelProperty(value = "道控产品ID")
    private Integer ProductID;

    @ApiModelProperty(value = "结算价")
    private BigDecimal SettleFee;

    @ApiModelProperty(value = "售卖价")
    private BigDecimal SellingFee;

    @ApiModelProperty(value = "订单数量")
    private Integer OrderQty;

    @ApiModelProperty(value = "凭证码")
    private String Code;

    @ApiModelProperty(value = "可核销总数量")
    private Integer CodeQty;

    /*@ApiModelProperty(value = "可核销总次数")
    private Integer CodeCnt;*/

    @ApiModelProperty(value = "已核销数量")
    private Integer UseQty;

    /*@ApiModelProperty(value = "已核销次数")
    private Integer UseCnt;*/

    @ApiModelProperty(value = "已退票数量")
    private Integer RefundQty;

    @ApiModelProperty(value = "已退次数")
    private Integer RefundCnt;

    @ApiModelProperty(value = "游玩开始时间")
    private String StartDate;

    @ApiModelProperty(value = "结束时间")
    private String EndDate;

    @ApiModelProperty(value = "状态(ticket.OrderInfoStatusEnum)")
    private Integer Status;

    @ApiModelProperty(value = "手机号")
    private String Mobile;

    @ApiModelProperty(value = "姓名")
    private String IDCardName;

    @ApiModelProperty(value = "身份证号")
    private String IDCardNo;

    @ApiModelProperty(value = "使用时间")
    private String UseTime;

    @ApiModelProperty(value = "核销时间")
    private String RefundTime;
}
