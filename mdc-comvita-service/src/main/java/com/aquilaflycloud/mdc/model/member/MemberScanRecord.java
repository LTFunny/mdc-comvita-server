package com.aquilaflycloud.mdc.model.member;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.easypay.PaymentTypeEnum;
import com.aquilaflycloud.mdc.enums.member.ConsumptionTicketStateEnum;
import com.aquilaflycloud.mdc.enums.member.MemberScanTypeEnum;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "member_scan_record")
public class MemberScanRecord implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 会员id
     */
    @TableField(value = "member_id")
    @ApiModelProperty(value="会员id")
    private Long memberId;

    /**
     * 手机号
     */
    @TableField(value = "phone_number")
    @ApiModelProperty(value="手机号")
    private String phoneNumber;

    /**
     * 微信或支付宝appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value="微信或支付宝appId")
    private String appId;

    /**
     * 微信用户id
     */
    @TableField(value = "open_id")
    @ApiModelProperty(value="微信用户id")
    private String openId;

    /**
     * 支付宝用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="支付宝用户id")
    private String userId;

    /**
     * 微信或支付宝昵称
     */
    @TableField(value = "nick_name")
    @ApiModelProperty(value="微信或支付宝昵称")
    private String nickName;

    /**
     * 微信或支付宝头像
     */
    @TableField(value = "avatar_url")
    @ApiModelProperty(value="微信或支付宝头像")
    private String avatarUrl;

    /**
     * 商户编号
     */
    @TableField(value = "merchant_no")
    @ApiModelProperty(value="商户编号")
    private String merchantNo;

    /**
     * 订单号
     */
    @TableField(value = "order_no")
    @ApiModelProperty(value="订单号")
    private String orderNo;

    /**
     * 微信或支付宝订单号
     */
    @TableField(value = "top_order_no")
    @ApiModelProperty(value="微信或支付宝订单号")
    private String topOrderNo;

    /**
     * 流水号
     */
    @TableField(value = "pay_no")
    @ApiModelProperty(value="流水号")
    private String payNo;

    /**
     * 银行卡号
     */
    @TableField(value = "card_no")
    @ApiModelProperty(value="银行卡号")
    private String cardNo;

    /**
     * 商品名称
     */
    @TableField(value = "product_name")
    @ApiModelProperty(value="商品名称")
    private String productName;

    /**
     * 实付金额
     */
    @TableField(value = "actual_money")
    @ApiModelProperty(value="实付金额")
    private BigDecimal actualMoney;

    /**
     * 订单金额
     */
    @TableField(value = "pay_money")
    @ApiModelProperty(value="订单金额")
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
    private PaymentTypeEnum payType;

    /**
     * 消费奖励
     */
    @TableField(value = "reward_value_content")
    @ApiModelProperty(value="消费奖励")
    private String rewardValueContent;

    @TableField(value = "ticket_url")
    @ApiModelProperty(value = "小票凭证url")
    private String ticketUrl;

    @TableField(value = "pay_url")
    @ApiModelProperty(value = "支付凭证url")
    private String payUrl;

    @TableField(value = "state")
    @ApiModelProperty(value = "状态(member.MemberScanStateEnum)")
    private ConsumptionTicketStateEnum state;

    @TableField(value = "feedback_content")
    @ApiModelProperty(value = "审核反馈")
    private String feedbackContent;

    @TableField(value = "shop_id")
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @TableField(value = "shop_name")
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @TableField(value = "shop_url")
    @ApiModelProperty(value = "店铺图片")
    private String shopUrl;

    @TableField(value = "type")
    @ApiModelProperty(value = "类型(member.MemberScanTypeEnum)")
    private MemberScanTypeEnum type;

    @TableField(value = "audit_id")
    @ApiModelProperty(value = "审核人id")
    private Long auditId;

    @TableField(value = "audit_name")
    @ApiModelProperty(value = "审核人名称")
    private String auditName;

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
