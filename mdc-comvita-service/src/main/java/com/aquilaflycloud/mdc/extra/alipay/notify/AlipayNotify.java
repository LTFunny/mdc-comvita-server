package com.aquilaflycloud.mdc.extra.alipay.notify;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AlipayNotify {
    @ApiModelProperty(value = "第三方应用的appId")
    private String authAppId;

    @ApiModelProperty(value = "通知时间")
    private String notifyTime;

    @ApiModelProperty(value = "通知类型")
    private String notifyType;

    @ApiModelProperty(value = "通知校验ID")
    private String notifyId;

    @ApiModelProperty(value = "签名类型")
    private String signType;

    @ApiModelProperty(value = "签名")
    private String sign;

    @ApiModelProperty(value = "支付宝交易号")
    private String tradeNo;

    @ApiModelProperty(value = "开发者的app_id")
    private String appId;

    @ApiModelProperty(value = "商户订单号")
    private String outTradeNo;

    @ApiModelProperty(value = "商户业务号")
    private String outBizNo;

    @ApiModelProperty(value = "买家支付宝用户号")
    private String buyerId;

    @ApiModelProperty(value = "买家支付宝账号")
    private String buyerLogonId;

    @ApiModelProperty(value = "卖家支付宝用户号")
    private String sellerId;

    @ApiModelProperty(value = "卖家支付宝账号")
    private String sellerEmail;

    @ApiModelProperty(value = "交易状态")
    private String tradeStatus;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "实收金额")
    private BigDecimal receiptAmount;

    @ApiModelProperty(value = "开票金额")
    private BigDecimal invoiceAmount;

    @ApiModelProperty(value = "付款金额")
    private BigDecimal buyerPayAmount;

    @ApiModelProperty(value = "集分宝金额")
    private BigDecimal pointAmount;

    @ApiModelProperty(value = "总退款金额")
    private BigDecimal refundFee;

    @ApiModelProperty(value = "实际退款金额")
    private BigDecimal sendBackFee;

    @ApiModelProperty(value = "订单标题")
    private String subject;

    @ApiModelProperty(value = "商品描述")
    private String body;

    @ApiModelProperty(value = "交易创建时间")
    private String gmtCreate;

    @ApiModelProperty(value = "交易付款时间")
    private String gmtPayment;

    @ApiModelProperty(value = "交易退款时间")
    private String gmtRefund;

    @ApiModelProperty(value = "交易结束时间")
    private String gmtClose;

    @ApiModelProperty(value = "支付金额信息")
    private String fundBillList;

    @ApiModelProperty(value = "编码格式")
    private String charset;

    @ApiModelProperty(value = "版本号")
    private String version;
}
