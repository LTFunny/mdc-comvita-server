package com.aquilaflycloud.mdc.model.exchange;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.exchange.*;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
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
@TableName(value = "exchange_order")
public class ExchangeOrder implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 订单编号
     */
    @TableField(value = "order_code")
    @ApiModelProperty(value = "订单编号")
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
     * 商品id
     */
    @TableField(value = "goods_id")
    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    /**
     * 商品类型
     */
    @TableField(value = "goods_type")
    @ApiModelProperty(value = "商品类型")
    private GoodsTypeEnum goodsType;

    /**
     * 商品名称
     */
    @TableField(value = "goods_name")
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品图片
     */
    @TableField(value = "goods_imgs")
    @ApiModelProperty(value = "商品图片")
    private String goodsImgs;

    /**
     * 售后说明
     */
    @TableField(value = "goods_service")
    @ApiModelProperty(value = "售后说明")
    private String goodsService;

    /**
     * 关联商品id
     */
    @TableField(value = "rel_id")
    @ApiModelProperty(value = "关联商品id")
    private Long relId;

    /**
     * 关联商品内容
     */
    @TableField(value = "rel_content")
    @ApiModelProperty(value = "关联商品内容")
    private String relContent;

    /**
     * 奖励类型
     */
    @TableField(value = "reward_type")
    @ApiModelProperty(value = "奖励类型")
    private RewardTypeEnum rewardType;

    /**
     * 单件奖励值
     */
    @TableField(value = "single_reward")
    @ApiModelProperty(value = "单件奖励值")
    private Integer singleReward;

    /**
     * 单件金额
     */
    @TableField(value = "single_price")
    @ApiModelProperty(value = "单件金额")
    private BigDecimal singlePrice;

    /**
     * 市场参考价
     */
    @TableField(value = "market_price")
    @ApiModelProperty(value = "市场参考价")
    private BigDecimal marketPrice;

    /**
     * 商品数量
     */
    @TableField(value = "goods_count")
    @ApiModelProperty(value = "商品数量")
    private Integer goodsCount;

    /**
     * 总奖励值
     */
    @TableField(value = "total_reward")
    @ApiModelProperty(value = "总奖励值")
    private Integer totalReward;

    /**
     * 总金额
     */
    @TableField(value = "total_price")
    @ApiModelProperty(value = "总金额")
    private BigDecimal totalPrice;

    /**
     * 支付方式
     */
    @TableField(value = "pay_mode")
    @ApiModelProperty(value = "支付方式")
    private PayModeEnum payMode;

    /**
     * 兑换时间
     */
    @TableField(value = "exchange_time")
    @ApiModelProperty(value = "兑换时间")
    private Date exchangeTime;

    /**
     * 退款类型
     */
    @TableField(value = "refund_type")
    @ApiModelProperty(value = "退款类型")
    private RefundTypeEnum refundType;

    /**
     * 是否过期自动退款
     */
    @TableField(value = "refund_expired")
    @ApiModelProperty(value = "是否过期自动退款")
    private WhetherEnum refundExpired;

    /**
     * 配送方式
     */
    @TableField(value = "delivery_type")
    @ApiModelProperty(value = "配送方式")
    private DeliveryTypeEnum deliveryType;

    /**
     * 自提地址
     */
    @TableField(value = "delivery_address")
    @ApiModelProperty(value = "自提地址")
    private String deliveryAddress;

    /**
     * 收货人姓名
     */
    @TableField(value = "receive_name")
    @ApiModelProperty(value = "收货人姓名")
    private String receiveName;

    /**
     * 收货手机
     */
    @TableField(value = "receive_phone")
    @ApiModelProperty(value = "收货手机")
    private String receivePhone;

    /**
     * 收货地址邮编
     */
    @TableField(value = "receive_postal_code")
    @ApiModelProperty(value = "收货地址邮编")
    private String receivePostalCode;

    /**
     * 收货国家
     */
    @TableField(value = "receive_country")
    @ApiModelProperty(value = "收货国家")
    private String receiveCountry;

    /**
     * 收货省份
     */
    @TableField(value = "receive_province")
    @ApiModelProperty(value = "收货省份")
    private String receiveProvince;

    /**
     * 收货城市
     */
    @TableField(value = "receive_city")
    @ApiModelProperty(value = "收货城市")
    private String receiveCity;

    /**
     * 收货区域
     */
    @TableField(value = "receive_county")
    @ApiModelProperty(value = "收货区域")
    private String receiveCounty;

    /**
     * 收货街道
     */
    @TableField(value = "receive_street")
    @ApiModelProperty(value = "收货街道")
    private String receiveStreet;

    /**
     * 收货详细地址
     */
    @TableField(value = "receive_address")
    @ApiModelProperty(value = "收货详细地址")
    private String receiveAddress;

    /**
     * 订单留言
     */
    @TableField(value = "order_remark")
    @ApiModelProperty(value = "订单留言")
    private String orderRemark;

    /**
     * 订单备注
     */
    @TableField(value = "order_operator_remark")
    @ApiModelProperty(value = "订单备注")
    private String orderOperatorRemark;

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
     * 快递金额
     */
    @TableField(value = "express_price")
    @ApiModelProperty(value = "快递金额")
    private BigDecimal expressPrice;

    /**
     * 退款原因类型
     */
    @TableField(value = "refund_reason_type")
    @ApiModelProperty(value = "退款原因类型")
    private String refundReasonType;

    /**
     * 退款原因
     */
    @TableField(value = "refund_reason")
    @ApiModelProperty(value = "退款原因")
    private String refundReason;

    /**
     * 退款时间
     */
    @TableField(value = "refund_time")
    @ApiModelProperty(value = "退款时间")
    private Date refundTime;

    /**
     * 支付时间
     */
    @TableField(value = "pay_time")
    @ApiModelProperty(value = "支付时间")
    private Date payTime;

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
     * 退款审核说明
     */
    @TableField(value = "refund_audit_remark")
    @ApiModelProperty(value = "退款审核说明")
    private String refundAuditRemark;

    /**
     * 订单状态
     */
    @TableField(value = "order_state")
    @ApiModelProperty(value = "订单状态")
    private OrderStateEnum orderState;

    /**
     * 退款状态
     */
    @TableField(value = "refund_state")
    @ApiModelProperty(value = "退款状态")
    private RefundStateEnum refundState;

    /**
     * 下单的skuId
     */
    @TableField(value = "sku_id")
    @ApiModelProperty(value = "下单的skuId")
    private Long skuId;

    /**
     * 规格ids
     */
    @TableField(value = "spec_ids")
    @ApiModelProperty(value = "规格ids")
    private String specIds;

    /**
     * 规格值ids
     */
    @TableField(value = "spec_value_ids")
    @ApiModelProperty(value = "规格值ids")
    private String specValueIds;

    /**
     * 配置json串
     */
    @TableField(value = "configure_json")
    @ApiModelProperty(value = "配置json串")
    private String configureJson;

    /**
     * 自编码
     */
    @TableField(value = "self_code")
    @ApiModelProperty(value = "自编码")
    private String selfCode;

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
     * 指定用户部门ids
     */
    @TableField(value = "designate_org_ids")
    @ApiModelProperty(value = "指定用户部门ids")
    private String designateOrgIds;

    /**
     * 指定用户部门名称
     */
    @TableField(value = "designate_org_names")
    @ApiModelProperty(value = "指定用户部门名称")
    private String designateOrgNames;

    /**
     * isv的appId
     */
    @TableField(value = "app_key", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "isv的appId", hidden = true)
    @JSONField(serialize = false)
    private String appKey;

    public ExchangeOrder() {
    }

    public void setSkuInfo(RewardTypeEnum rewardType, Integer singleReward, BigDecimal singlePrice, Long skuId, String specIds, String specValueIds, String configureJson, String selfCode) {
        this.rewardType = rewardType;
        this.singleReward = singleReward;
        this.singlePrice = singlePrice;
        this.skuId = skuId;
        this.specIds = specIds;
        this.specValueIds = specValueIds;
        this.configureJson = configureJson;
        this.selfCode = selfCode;
    }

    private static final long serialVersionUID = 1L;
}
