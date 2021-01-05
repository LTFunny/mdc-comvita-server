package com.aquilaflycloud.mdc.model.pre;

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

/**
 * 预售订单售后信息表
 */
@Data
@TableName(value = "pre_refund_order_info")
public class PreRefundOrderInfo implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 售后单编码
     */
    @TableField(value = "refund_code")
    @ApiModelProperty(value = "售后单编码")
    private String refundCode;

    /**
     * 退款金额
     */
    @TableField(value = "refund_price")
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundPrice;

    /**
     * 售后商品状态
     */
    @TableField(value = "refund_goods_state")
    @ApiModelProperty(value = "售后商品状态")
    private Integer refundGoodsState;

    /**
     * 售后时间
     */
    @TableField(value = "refund_time")
    @ApiModelProperty(value = "售后时间")
    private Date refundTime;

    /**
     * 售后原因
     */
    @TableField(value = "refund_reason")
    @ApiModelProperty(value = "售后原因")
    private String refundReason;

    /**
     * 订单id
     */
    @TableField(value = "order_id")
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    /**
     * 订单编码
     */
    @TableField(value = "order_code")
    @ApiModelProperty(value = "订单编码")
    private String orderCode;

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
     * 门店id
     */
    @TableField(value = "shop_id")
    @ApiModelProperty(value = "门店id")
    private Long shopId;

    /**
     * 门店名称
     */
    @TableField(value = "shop_name")
    @ApiModelProperty(value = "门店名称")
    private String shopName;

    /**
     * 导购员id
     */
    @TableField(value = "guide_id")
    @ApiModelProperty(value = "导购员id")
    private Long guideId;

    /**
     * 导购员名称
     */
    @TableField(value = "guide_name")
    @ApiModelProperty(value = "导购员名称")
    private String guideName;

    /**
     * 总金额
     */
    @TableField(value = "total_price")
    @ApiModelProperty(value = "总金额")
    private BigDecimal totalPrice;

    /**
     * 销售小票url
     */
    @TableField(value = "ticket_url")
    @ApiModelProperty(value = "销售小票url")
    private String ticketUrl;

    /**
     * 快递编码
     */
    @TableField(value = "express_code")
    @ApiModelProperty(value = "快递编码")
    private String expressCode;

    /**
     * 快递名称
     */
    @TableField(value = "express_name")
    @ApiModelProperty(value = "快递名称")
    private String expressName;

    /**
     * 快递单号
     */
    @TableField(value = "express_order")
    @ApiModelProperty(value = "快递单号")
    private String expressOrder;

    /**
     * 发货时间
     */
    @TableField(value = "delivery_time")
    @ApiModelProperty(value = "发货时间")
    private Date deliveryTime;

    /**
     * 收货时间
     */
    @TableField(value = "receive_time")
    @ApiModelProperty(value = "收货时间")
    private Date receiveTime;

    /**
     * 确认时间
     */
    @TableField(value = "confirm_time")
    @ApiModelProperty(value = "确认时间")
    private Date confirmTime;

    /**
     * 买家姓名
     */
    @TableField(value = "buyer_name")
    @ApiModelProperty(value = "买家姓名")
    private String buyerName;

    /**
     * 买家手机
     */
    @TableField(value = "buyer_phone")
    @ApiModelProperty(value = "买家手机")
    private String buyerPhone;

    /**
     * 买家地址邮编
     */
    @TableField(value = "buyer_postal_code")
    @ApiModelProperty(value = "买家地址邮编")
    private String buyerPostalCode;

    /**
     * 买家详细地址
     */
    @TableField(value = "buyer_address")
    @ApiModelProperty(value = "买家详细地址")
    private String buyerAddress;

    /**
     * 订单状态
     */
    @TableField(value = "order_state")
    @ApiModelProperty(value = "订单状态")
    private Integer orderState;

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
     * 创建记录人id
     */
    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建记录人id", hidden = true)
    @JSONField(serialize = false)
    private Long creatorId;

    /**
     * 创建记录人名称
     */
    @TableField(value = "creator_name", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建记录人名称")
    private String creatorName;

    /**
     * 创建用户所属部门ids
     */
    @TableField(value = "creator_org_ids", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建用户所属部门ids", hidden = true)
    @JSONField(serialize = false)
    private String creatorOrgIds;

    /**
     * 创建用户所属部门名称
     */
    @TableField(value = "creator_org_names", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建用户所属部门名称")
    private String creatorOrgNames;

    /**
     * 最后操作人id
     */
    @TableField(value = "last_operator_id", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后操作人id", hidden = true)
    @JSONField(serialize = false)
    private Long lastOperatorId;

    /**
     * 最后操作人名称
     */
    @TableField(value = "last_operator_name", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后操作人名称")
    private String lastOperatorName;

    /**
     * isv的appId
     */
    @TableField(value = "app_key", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "isv的appId", hidden = true)
    @JSONField(serialize = false)
    private String appKey;

    private static final long serialVersionUID = 1L;
}
