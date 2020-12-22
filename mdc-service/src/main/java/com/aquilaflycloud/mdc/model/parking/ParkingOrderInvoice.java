package com.aquilaflycloud.mdc.model.parking;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.parking.InvoiceTypeEnum;
import com.aquilaflycloud.mdc.enums.parking.PayTypeEnum;
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
@TableName(value = "parking_order_invoice")
public class ParkingOrderInvoice implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 会员id
     */
    @TableField(value = "member_id")
    @ApiModelProperty(value = "会员id")
    private Long memberId;

    /**
     * 手机号
     */
    @TableField(value = "phone_number")
    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    /**
     * 微信或支付宝appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    /**
     * 微信用户id
     */
    @TableField(value = "open_id")
    @ApiModelProperty(value = "微信用户id")
    private String openId;

    /**
     * 支付宝用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value = "支付宝用户id")
    private String userId;

    /**
     * 微信或支付宝昵称
     */
    @TableField(value = "nick_name")
    @ApiModelProperty(value = "微信或支付宝昵称")
    private String nickName;

    /**
     * 微信或支付宝头像
     */
    @TableField(value = "avatar_url")
    @ApiModelProperty(value = "微信或支付宝头像")
    private String avatarUrl;

    /**
     * 订单id
     */
    @TableField(value = "order_id")
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    /**
     * 订单号
     */
    @TableField(value = "order_no")
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 物理卡号
     */
    @TableField(value = "card_id")
    @ApiModelProperty(value = "物理卡号")
    private String cardId;

    /**
     * 车牌号码
     */
    @TableField(value = "car_no")
    @ApiModelProperty(value = "车牌号码")
    private String carNo;

    /**
     * 支付时间
     */
    @TableField(value = "pay_time")
    @ApiModelProperty(value = "支付时间")
    private Date payTime;

    /**
     * 支付类型
     */
    @TableField(value = "pay_type")
    @ApiModelProperty(value = "支付类型")
    private PayTypeEnum payType;

    /**
     * 总金额
     */
    @TableField(value = "total_fee")
    @ApiModelProperty(value = "总金额")
    private BigDecimal totalFee;

    /**
     * 开票类型
     */
    @TableField(value = "invoice_type")
    @ApiModelProperty(value = "开票类型")
    private InvoiceTypeEnum invoiceType;

    /**
     * 购方税号
     */
    @TableField(value = "buyer_no")
    @ApiModelProperty(value = "购方税号")
    private String buyerNo;

    /**
     * 购方地址
     */
    @TableField(value = "buyer_address")
    @ApiModelProperty(value = "购方地址")
    private String buyerAddress;

    /**
     * 购方电话
     */
    @TableField(value = "buyer_phone")
    @ApiModelProperty(value = "购方电话")
    private String buyerPhone;

    /**
     * 购方开户行
     */
    @TableField(value = "buyer_bank")
    @ApiModelProperty(value = "购方开户行")
    private String buyerBank;

    /**
     * 购方账户
     */
    @TableField(value = "buyer_account")
    @ApiModelProperty(value = "购方账户")
    private String buyerAccount;

    /**
     * 购方客户名称
     */
    @TableField(value = "buyer_name")
    @ApiModelProperty(value = "购方客户名称")
    private String buyerName;

    /**
     * 发票下载地址
     */
    @TableField(value = "invoice_url")
    @ApiModelProperty(value = "发票下载地址")
    private String invoiceUrl;

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