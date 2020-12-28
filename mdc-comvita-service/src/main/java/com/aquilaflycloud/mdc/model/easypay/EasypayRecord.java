package com.aquilaflycloud.mdc.model.easypay;

import com.alibaba.fastjson.annotation.JSONField;
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
@TableName(value = "easypay_record")
public class EasypayRecord implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 会员编码
     */
    @TableField(value = "member_code")
    @ApiModelProperty(value="会员编码")
    private String memberCode;

    /**
     * 微信appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value="微信appId")
    private String appId;

    /**
     * 微信openId
     */
    @TableField(value = "open_id")
    @ApiModelProperty(value="微信openId")
    private String openId;

    /**
     * 微信unionId
     */
    @TableField(value = "union_id")
    @ApiModelProperty(value="微信unionId")
    private String unionId;

    /**
     * 支付宝appId
     */
    @TableField(value = "ali_app_id")
    @ApiModelProperty(value="支付宝appId")
    private String aliAppId;

    /**
     * 支付宝userId
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="支付宝userId")
    private String userId;

    /**
     * MDC商户号
     */
    @TableField(value = "shop_id")
    @ApiModelProperty(value="MDC商户号")
    private String shopId;

    /**
     * 商户编号
     */
    @TableField(value = "merchant_no")
    @ApiModelProperty(value="商户编号")
    private String merchantNo;

    /**
     * 商户名称
     */
    @TableField(value = "merchant_name")
    @ApiModelProperty(value="商户名称")
    private String merchantName;

    /**
     * 商品名称
     */
    @TableField(value = "product_name")
    @ApiModelProperty(value="商品名称")
    private String productName;

    /**
     * 订单号
     */
    @TableField(value = "order_no")
    @ApiModelProperty(value="订单号")
    private String orderNo;

    /**
     * 流水号
     */
    @TableField(value = "pay_no")
    @ApiModelProperty(value="流水号")
    private String payNo;

    /**
     * 银行卡号
     */
    @TableField(value = "credit_card")
    @ApiModelProperty(value="银行卡号")
    private String creditCard;

    /**
     * 发卡银行
     */
    @TableField(value = "bank_name")
    @ApiModelProperty(value="发卡银行")
    private String bankName;

    /**
     * 货币类型
     */
    @TableField(value = "money_type")
    @ApiModelProperty(value="货币类型")
    private String moneyType;

    /**
     * 消费金额
     */
    @TableField(value = "pay_money")
    @ApiModelProperty(value="消费金额")
    private BigDecimal payMoney;

    /**
     * 消费时间
     */
    @TableField(value = "pay_time")
    @ApiModelProperty(value="消费时间")
    private Date payTime;

    /**
     * 支付方式
     */
    @TableField(value = "pay_type")
    @ApiModelProperty(value="支付方式")
    private String payType;

    /**
     * 操作类型
     */
    @TableField(value = "action_type")
    @ApiModelProperty(value="操作类型")
    private String actionType;

    /**
     * 操作时间
     */
    @TableField(value = "action_time")
    @ApiModelProperty(value="操作时间")
    private Date actionTime;

    /**
     * 第三方支付记录数据
     */
    @TableField(value = "all_data")
    @ApiModelProperty(value="第三方支付记录数据")
    private String allData;

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