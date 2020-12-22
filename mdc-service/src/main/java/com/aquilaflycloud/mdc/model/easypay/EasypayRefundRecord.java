package com.aquilaflycloud.mdc.model.easypay;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.easypay.EasyPayAccountTypeEnum;
import com.aquilaflycloud.mdc.enums.easypay.OrderTypeEnum;
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
@TableName(value = "easypay_refund_record")
public class EasypayRefundRecord implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

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
     * 商户退款订单号
     */
    @TableField(value = "refund_order_no")
    @ApiModelProperty(value = "商户退款订单号")
    private String refundOrderNo;

    /**
     * 退款金额(元)
     */
    @TableField(value = "refund_amount")
    @ApiModelProperty(value = "退款金额(元)")
    private BigDecimal refundAmount;

    /**
     * 退款原因描述
     */
    @TableField(value = "refund_reason")
    @ApiModelProperty(value = "退款原因描述")
    private String refundReason;

    /**
     * 退款申请状态
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "退款申请状态")
    private String status;

    /**
     * 响应码
     */
    @TableField(value = "code")
    @ApiModelProperty(value = "响应码")
    private String code;

    /**
     * 响应码描述
     */
    @TableField(value = "code_msg")
    @ApiModelProperty(value = "响应码描述")
    private String codeMsg;

    /**
     * 退款状态
     */
    @TableField(value = "notify_status")
    @ApiModelProperty(value = "退款状态")
    private String notifyStatus;

    /**
     * 退款成功状态
     */
    @TableField(value = "refund_state")
    @ApiModelProperty(value = "退款成功状态")
    private WhetherEnum refundState;

    /**
     * 退款参数
     */
    @TableField(value = "refund_param_content")
    @ApiModelProperty(value = "退款参数")
    private String refundParamContent;

    /**
     * 退款结果
     */
    @TableField(value = "refund_result_content")
    @ApiModelProperty(value = "退款结果")
    private String refundResultContent;

    /**
     * 退款结果通知
     */
    @TableField(value = "refund_notify_content")
    @ApiModelProperty(value = "退款结果通知")
    private String refundNotifyContent;

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