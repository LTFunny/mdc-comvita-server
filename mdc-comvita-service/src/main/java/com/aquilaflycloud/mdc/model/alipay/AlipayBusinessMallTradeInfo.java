package com.aquilaflycloud.mdc.model.alipay;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName(value = "alipay_business_mall_trade_info")
public class AlipayBusinessMallTradeInfo {
    @ApiModelProperty(value = "主键")
    @TableId
    private Long id;

    @ApiModelProperty(value = "通知ID")
    @TableField(value = "notify_id")
    private String notifyId;

    @ApiModelProperty(value = "消息发送时的服务端时间")
    @TableField(value = "notify_timestamp")
    private String notifyTimestamp;

    @ApiModelProperty(value = "开发者的app_id")
    @TableField(value = "app_id")
    private String appId;

    @ApiModelProperty(value = "消息类型。目前支持类型：sys：系统消息；usr，用户消息；app，应用消息")
    @TableField(value = "msg_type")
    private String msgType;

    @ApiModelProperty(value = "消息归属的商户支付宝uid。用户消息和应用消息时非空")
    @TableField(value = "msg_uid")
    private String msgUid;

    @ApiModelProperty(value = "消息归属方的应用id。应用消息时非空")
    @TableField(value = "msg_app_id")
    private String msgAppId;

    @ApiModelProperty(value = "版本号")
    @TableField(value = "version")
    private String version;

    @ApiModelProperty(value = "签名")
    @TableField(value = "sign")
    private String sign;

    @ApiModelProperty(value = "签名类型")
    @TableField(value = "sign_type")
    private String signType;

    @ApiModelProperty(value = "加密算法")
    @TableField(value = "encrypt_type")
    private String encryptType;

    @ApiModelProperty(value = "编码格式")
    @TableField(value = "charset")
    private String charset;

    @ApiModelProperty(value = "通知类型")
    @TableField(value = "notify_type")
    private String notifyType;

    @ApiModelProperty(value = "通知时间")
    @TableField(value = "notify_time")
    private String notifyTime;

    @ApiModelProperty(value = "授权方的应用id")
    @TableField(value = "auth_app_id")
    private String authAppId;

    @ApiModelProperty(value = "支付宝交易号")
    @TableField(value = "trade_no")
    private String tradeNo;

    @ApiModelProperty(value = "商户订单号")
    @TableField(value = "out_trade_no")
    private String outTradeNo;

    @ApiModelProperty(value = "交易总金额")
    @TableField(value = "total_amount")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "买家实付金额")
    @TableField(value = "buyer_pay_amount")
    private BigDecimal buyerPayAmount;

    @ApiModelProperty(value = "商家实收金额")
    @TableField(value = "receipt_amount")
    private BigDecimal receiptAmount;

    @ApiModelProperty(value = "交易付款时间")
    @TableField(value = "gmt_payment")
    private String gmtPayment;

    @ApiModelProperty(value = "支付宝用户id")
    @TableField(value = "buyer_id")
    private String buyerId;

    @ApiModelProperty(value = "商圈中本次交易发生的商铺的识别类型，可取值：SMID,SHOPID,STOREID")
    @TableField(value = "mall_cell_type")
    private String mallCellType;

    @ApiModelProperty(value = "商圈中本次交易发生的商铺的识别号，在同一商铺类型下唯一")
    @TableField(value = "mall_cell_id")
    private String mallCellId;

    @ApiModelProperty(value = "发生交易的商圈(非商圈组)的partnerId")
    @TableField(value = "mall_pid")
    private String mallPid;

    @ApiModelProperty(value = "发生交易的商圈(非商圈组)的商圈唯一标识号")
    @TableField(value = "mall_id")
    private String mallId;

    @ApiModelProperty(value = "发生交易的商圈(非商圈组)的名称")
    @TableField(value = "mall_name")
    private String mallName;

    @ApiModelProperty(value = "接收当前消息的partnerId，用于接受方自检当前消息是否需要消费")
    @TableField(value = "notify_receive_pid")
    private String notifyReceivePid;

    public AlipayBusinessMallTradeInfo(String charset, String notifyTimestamp, String version, String signType, String notifyId, String appId, String sign) {
        this.notifyId = notifyId;
        this.notifyTimestamp = notifyTimestamp;
        this.appId = appId;
        this.version = version;
        this.sign = sign;
        this.signType = signType;
        this.charset = charset;
    }

    public AlipayBusinessMallTradeInfo() {
    }
}
