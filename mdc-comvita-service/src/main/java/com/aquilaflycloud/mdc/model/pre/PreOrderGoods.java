package com.aquilaflycloud.mdc.model.pre;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.pre.*;
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
 * 预售订单商品详情信息表
 */
@Data
@TableName(value = "pre_order_goods")
public class PreOrderGoods implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 订单id
     */
    @TableField(value = "order_id")
    @ApiModelProperty(value = "订单id")
    private Long orderId;
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
     * 商品id
     */
    @TableField(value = "goods_id")
    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    /**
     * 提货卡id
     */
    @TableField(value = "card_id")
    @ApiModelProperty(value = "提货卡id")
    private Long cardId;
    /**
     * 订单编码
     */
    @TableField(value = "order_code")
    @ApiModelProperty(value = "订单编码")
    private String orderCode;
    /**
     * 订单商品状态
     */
    @TableField(value = "order_goods_state")
    @ApiModelProperty(value = "订单商品状态")
    private OrderGoodsStateEnum orderGoodsState;

    /**
     * 商品编号
     */
    @TableField(value = "goods_code")
    @ApiModelProperty(value = "商品编号")
    private String goodsCode;

    /**
     * 商品名称
     */
    @TableField(value = "goods_name")
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品类型(1-预售商品、2-赠品、3-普通商品)
     */
    @TableField(value = "goods_type")
    @ApiModelProperty(value = "商品类型(1-预售商品、2-赠品、3-普通商品)")
    private GoodsTypeEnum goodsType;

    /**
     * 零售价
     */
    @TableField(value = "goods_price")
    @ApiModelProperty(value = "零售价")
    private BigDecimal goodsPrice;

    /**
     * 商品描述
     */
    @TableField(value = "goods_description")
    @ApiModelProperty(value = "商品描述")
    private String goodsDescription;

    /**
     * 商品照片
     */
    @TableField(value = "goods_picture")
    @ApiModelProperty(value = "商品照片")
    private String goodsPicture;


    /**
     * 收货地址-省
     */
    @TableField(value = "delivery_province")
    @ApiModelProperty(value = "收货地址-省")
    private String deliveryProvince;

    /**
     * 收货地址-市
     */
    @TableField(value = "delivery_city")
    @ApiModelProperty(value = "收货地址-市")
    private String deliveryCity;

    /**
     * 收货地址-区
     */
    @TableField(value = "delivery_district")
    @ApiModelProperty(value = "收货地址-区")
    private String deliveryDistrict;

    /**
     * 收货地址-详细地址
     */
    @TableField(value = "delivery_address")
    @ApiModelProperty(value = "收货地址-详细地址")
    private String deliveryAddress;

    /**
     * 发货时间
     */
    @TableField(value = "delivery_time")
    @ApiModelProperty(value = "发货时间")
    private Date deliveryTime;
    /**
     * 快递名称
     */
    @TableField(value = "express_name")
    @ApiModelProperty(value = "快递名称")
    private String expressName;
    /**
     * 快递单号
     */
    @TableField(value = "express_order_code")
    @ApiModelProperty(value = "快递单号")
    private String expressOrderCode;
    /**
     * 快递编码
     */
    @TableField(value = "express_code")
    @ApiModelProperty(value = "快递编码")
    private String expressCode;
    /**
     * 提货卡状态
     */
    @TableField(value = "picking_card_state")
    @ApiModelProperty(value = "提货卡状态")
    private PickingCardStateEnum pickingCardState;

    /**
     * 是否可修改 (0.是 1.否)
     */
    @TableField(value = "is_update")
    @ApiModelProperty(value = "是否可修改")
    private IsUpdateEnum isUpdate;

    /**
     * 预约人id(会员id)
     */
    @TableField(value = "reserve_id")
    @ApiModelProperty(value = "预约人id(会员id)")
    private Long reserveId;

    /**
     * 预约人名称
     */
    @TableField(value = "reserve_name")
    @ApiModelProperty(value = "预约人名称")
    private String reserveName;

    /**
     * 预约人电话
     */
    @TableField(value = "reserve_phone")
    @ApiModelProperty(value = "预约人电话")
    private String reservePhone;

    /**
     * 预约门店
     */
    @TableField(value = "reserve_shop")
    @ApiModelProperty(value = "预约门店")
    private String reserveShop;

    @TableField(value = "reserve_shop_id")
    @ApiModelProperty(value = "预约门店id")
    private String reserveShopId;
    /**
     * 预约开始时间
     */
    @TableField(value = "reserve_start_time")
    @ApiModelProperty(value = "预约开始时间")
    private Date reserveStartTime;

    /**
     * 预约结束时间
     */
    @TableField(value = "reserve_end_time")
    @ApiModelProperty(value = "预约结束时间")
    private Date reserveEndTime;

    /**
     * 提货卡号
     */
    @TableField(value = "card_code")
    @ApiModelProperty(value = "提货卡号")
    private String cardCode;

    /**
     * 提货卡密码
     */
    @TableField(value = "card_psw")
    @ApiModelProperty(value = "提货卡密码")
    private String cardPsw;

    /**
     * 提货时间
     */
    @TableField(value = "take_time")
    @ApiModelProperty(value = "提货时间")
    private Date takeTime;

    /**
     * 核销人id
     */
    @TableField(value = "verificater_id")
    @ApiModelProperty(value = "核销人id")
    private Long verificaterId;

    /**
     * 核销人名称
     */
    @TableField(value = "verificater_name")
    @ApiModelProperty(value = "核销人名称")
    private String verificaterName;

    /**
     * 核销人所属部门ids
     */
    @TableField(value = "verificater_org_ids")
    @ApiModelProperty(value = "核销人所属部门ids")
    private String verificaterOrgIds;

    /**
     * 核销人所属部门名称
     */
    @TableField(value = "verificater_org_names")
    @ApiModelProperty(value = "核销人所属部门名称")
    private String verificaterOrgNames;

    /**
     * 赠品标识(当商品都发货了,为1)
     */
    @TableField(value = "gifts_symbol")
    @ApiModelProperty(value = "赠品标识(当商品都发货了,为1)")
    private GiftsSymbolEnum giftsSymbol;

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
