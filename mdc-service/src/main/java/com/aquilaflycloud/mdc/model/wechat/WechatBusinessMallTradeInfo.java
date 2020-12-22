package com.aquilaflycloud.mdc.model.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "wechat_business_mall_trade_info")
public class WechatBusinessMallTradeInfo implements Serializable {
    @TableId(value = "id")
    @ApiModelProperty(value = "主键")
    private Long id;

    @TableField(value = "notify_id")
    @ApiModelProperty(value = "通知ID")
    private String notifyId;

    @TableField(value = "notify_create_time")
    @ApiModelProperty(value = "通知创建时间")
    private String notifyCreateTime;

    @TableField(value = "event_type")
    @ApiModelProperty(value = "通知类型，支付成功通知的类型为TRANSACTION.SUCCESS")
    private String eventType;

    @TableField(value = "resource_type")
    @ApiModelProperty(value = "通知数据类型，支付成功通知为encrypt-resource")
    private String resourceType;

    @TableField(value = "summary")
    @ApiModelProperty(value = "回调摘要")
    private String summary;

    @TableField(value = "algorithm_type")
    @ApiModelProperty(value = "加密算法类型，目前只支持AEAD_AES_256_GCM")
    private String algorithmType;

    @TableField(value = "original_type")
    @ApiModelProperty(value = "原始回调类型")
    private String originalType;

    @TableField(value = "associated_data")
    @ApiModelProperty(value = "附加数据")
    private String associatedData;

    @TableField(value = "nonce")
    @ApiModelProperty(value = "加密使用的随机串")
    private String nonce;

    @TableField(value = "mchid")
    @ApiModelProperty(value = "商圈商户号")
    private String mchid;

    @TableField(value = "merchant_name")
    @ApiModelProperty(value = "商圈商户名称")
    private String merchantName;

    @TableField(value = "shop_name")
    @ApiModelProperty(value = "门店名称")
    private String shopName;

    @TableField(value = "shop_number")
    @ApiModelProperty(value = "门店编号")
    private String shopNumber;

    @TableField(value = "open_id")
    @ApiModelProperty(value = "用户openid，顾客授权时使用的小程序上的openid")
    private String openid;

    @TableField(value = "app_id")
    @ApiModelProperty(value = "顾客授权时使用的小程序appid")
    private String appid;

    @TableField(value = "time_end")
    @ApiModelProperty(value = "交易完成时间")
    private String timeEnd;

    @TableField(value = "amount")
    @ApiModelProperty(value = "用户实际消费金额(分)")
    private Integer amount;

    @TableField(value = "transaction_id")
    @ApiModelProperty(value = "微信支付订单号")
    private String transactionId;

    @TableField(value = "commit_tag")
    @ApiModelProperty(value = "手动提交积分标记，自动提交无该字段，用于区分用户手动申请后推送的积分数据")
    private String commitTag;

    @TableField(value = "refund_time")
    @ApiModelProperty(value = "退款完成时间")
    private String refundTime;

    @TableField(value = "pay_amount")
    @ApiModelProperty(value = "用户实际消费金额，单位（分）")
    private Integer payAmount;

    @TableField(value = "refund_amount")
    @ApiModelProperty(value = "用户退款金额，单位（分）")
    private Integer refundAmount;

    @TableField(value = "refund_id")
    @ApiModelProperty(value = "微信支付退款单号")
    private String refundId;

    @TableField(value = "reward_value_content")
    @ApiModelProperty(value = "奖励值内容")
    private String rewardValueContent;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "系统创建时间")
    private Date createTime;

    @TableField(value = "tenant_id")
    @ApiModelProperty(value = "租户id", hidden = true)
    @JSONField(serialize = false)
    private Long tenantId;

    @TableField(value = "sub_tenant_id")
    @ApiModelProperty(value = "子租户id", hidden = true)
    @JSONField(serialize = false)
    private Long subTenantId;
}


