package com.aquilaflycloud.mdc.model.easypay;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.easypay.EasyPayAccountTypeEnum;
import com.aquilaflycloud.mdc.enums.easypay.OrderTypeEnum;
import com.aquilaflycloud.mdc.enums.easypay.PayTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "easypay_payment_record")
public class EasypayPaymentRecord implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 支付类型
     */
    @TableField(value = "pay_type")
    @ApiModelProperty(value = "支付类型")
    private PayTypeEnum payType;

    /**
     * 订单类型
     */
    @TableField(value = "order_type")
    @ApiModelProperty(value = "订单类型")
    private OrderTypeEnum orderType;

    /**
     * 订单id
     */
    @TableField(value = "order_id")
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    /**
     * 支付账号类型
     */
    @TableField(value = "account_type")
    @ApiModelProperty(value = "支付账号类型")
    private EasyPayAccountTypeEnum accountType;

    /**
     * 商户编号
     */
    @TableField(value = "merchant_no")
    @ApiModelProperty(value = "商户编号")
    private String merchantNo;

    /**
     * 订单号
     */
    @TableField(value = "order_no")
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 支付金额(元)
     */
    @TableField(value = "amount")
    @ApiModelProperty(value = "支付金额(元)")
    private BigDecimal amount;

    /**
     * 交易类型
     */
    @TableField(value = "frp_code")
    @ApiModelProperty(value = "交易类型")
    private String frpCode;

    /**
     * 交易流水
     */
    @TableField(value = "trx_no")
    @ApiModelProperty(value = "交易流水")
    private String trxNo;

    /**
     * 微信或支付宝appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    /**
     * 微信openId
     */
    @TableField(value = "open_id")
    @ApiModelProperty(value = "微信openId")
    private String openId;

    /**
     * 支付宝userId
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value = "支付宝userId")
    private String userId;

    /**
     * 返回码
     */
    @TableField(value = "code")
    @ApiModelProperty(value = "返回码")
    private String code;

    /**
     * 返回码描述
     */
    @TableField(value = "code_msg")
    @ApiModelProperty(value = "返回码描述")
    private String codeMsg;

    /**
     * 结果
     */
    @TableField(value = "result")
    @ApiModelProperty(value = "结果")
    private String result;

    /**
     * 二维码图片码
     */
    @TableField(value = "pic")
    @ApiModelProperty(value = "二维码图片码")
    private String pic;

    /**
     * 支付状态
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "支付状态")
    private String status;

    /**
     * 支付成功状态
     */
    @TableField(value = "pay_state")
    @ApiModelProperty(value = "支付成功状态")
    private WhetherEnum payState;

    /**
     * 支付时间
     */
    @TableField(value = "pay_time")
    @ApiModelProperty(value = "支付时间")
    private Date payTime;

    /**
     * 交易结果通知时间
     */
    @TableField(value = "deal_time")
    @ApiModelProperty(value = "交易结果通知时间")
    private Date dealTime;

    /**
     * 支付参数
     */
    @TableField(value = "pay_param_content")
    @ApiModelProperty(value = "支付参数")
    private String payParamContent;

    /**
     * 支付结果
     */
    @TableField(value = "pay_result_content")
    @ApiModelProperty(value = "支付结果")
    private String payResultContent;

    /**
     * 支付结果通知
     */
    @TableField(value = "pay_notify_content")
    @ApiModelProperty(value = "支付结果通知")
    private String payNotifyContent;

    /**
     * 支付查询结果
     */
    @TableField(value = "pay_query_content")
    @ApiModelProperty(value = "支付查询结果")
    private String payQueryContent;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "last_update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后更新时间")
    private Date lastUpdateTime;

    /**
     * 租户id
     */
    @TableField(value = "tenant_id")
    @ApiModelProperty(value = "租户id", hidden = true)
    @JSONField(serialize = false)
    private Long tenantId;

    /**
     * 子租户id
     */
    @TableField(value = "sub_tenant_id")
    @ApiModelProperty(value = "子租户id", hidden = true)
    @JSONField(serialize = false)
    private Long subTenantId;

    /**
     * isv的appId
     */
    @TableField(value = "app_key", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "isv的appId", hidden = true)
    @JSONField(serialize = false)
    private String appKey;

    private static final long serialVersionUID = 1L;
}