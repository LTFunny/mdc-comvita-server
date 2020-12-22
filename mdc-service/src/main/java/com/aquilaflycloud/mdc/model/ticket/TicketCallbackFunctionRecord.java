package com.aquilaflycloud.mdc.model.ticket;

import com.aquilaflycloud.mdc.enums.ticket.OrderInfoStatusEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 回调函数记录表
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-19
 */
@Data
@TableName(value = "ticket_callback_function_record")
public class TicketCallbackFunctionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "商户订单号入参")
    @TableField("order_no_param")
    private String orderNoParam;

    @ApiModelProperty(value = "OTA订单号入参")
    @TableField("OTA_order_no_param")
    private String otaOrderNoParam;

    @ApiModelProperty(value = "商户编码")
    @TableField("merchant_code")
    private String merchantCode;

    @ApiModelProperty(value = "商户名称")
    @TableField("merchant_name")
    private String merchantName;

    @ApiModelProperty(value = "商户订单号")
    @TableField("order_no")
    private String orderNo;

    @ApiModelProperty(value = "OTA 订单号")
    @TableField("out_order_no")
    private String outOrderNo;

    @ApiModelProperty(value = "产品ID")
    @TableField("product_id")
    private Integer productId;

    @ApiModelProperty(value = "结算价")
    @TableField("settle_fee")
    private BigDecimal settleFee;

    @ApiModelProperty(value = "售卖价")
    @TableField("selling_fee")
    private BigDecimal sellingFee;

    @ApiModelProperty(value = "订单数量")
    @TableField("order_qty")
    private Integer orderQty;

    @ApiModelProperty(value = "凭证码")
    @TableField("ecode")
    private String ecode;

    @ApiModelProperty(value = "可核销总数量")
    @TableField("code_qty")
    private Integer codeQty;

    @ApiModelProperty(value = "已核销数量")
    @TableField("use_qty")
    private Integer useQty;

    @ApiModelProperty(value = "已退票数量")
    @TableField("refund_qty")
    private Integer refundQty;

    @ApiModelProperty(value = "已退次数")
    @TableField("refund_cnt")
    private Integer refundCnt;

    @ApiModelProperty(value = "游玩开始时间")
    @TableField("start_date")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    @TableField("end_date")
    private Date endDate;

    @ApiModelProperty(value = "状态(ticket.OrderInfoStatusEnum)")
    @TableField("status")
    private OrderInfoStatusEnum status;

    @ApiModelProperty(value = "手机号")
    @TableField("mobile")
    private String mobile;

    @ApiModelProperty(value = "姓名")
    @TableField("id_card_name")
    private String idCardName;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    public TicketCallbackFunctionRecord(String orderNoParam, String otaOrderNoParam, String merchantCode, String merchantName, String orderNo, String outOrderNo, Integer productId, BigDecimal settleFee, BigDecimal sellingFee, Integer orderQty, String ecode, Integer codeQty, Integer useQty, Integer refundQty, Integer refundCnt, Date startDate, Date endDate, OrderInfoStatusEnum status, String mobile, String idCardName) {
        this.orderNoParam = orderNoParam;
        this.otaOrderNoParam = otaOrderNoParam;
        this.merchantCode = merchantCode;
        this.merchantName = merchantName;
        this.orderNo = orderNo;
        this.outOrderNo = outOrderNo;
        this.productId = productId;
        this.settleFee = settleFee;
        this.sellingFee = sellingFee;
        this.orderQty = orderQty;
        this.ecode = ecode;
        this.codeQty = codeQty;
        this.useQty = useQty;
        this.refundQty = refundQty;
        this.refundCnt = refundCnt;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.mobile = mobile;
        this.idCardName = idCardName;
    }

    public TicketCallbackFunctionRecord() {
    }
}
