package com.aquilaflycloud.mdc.model.parking;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.parking.OrderStateEnum;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "parking_order")
public class ParkingOrder implements Serializable {
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
     * 短车卡号
     */
    @TableField(value = "short_card_id")
    @ApiModelProperty(value = "短车卡号")
    private String shortCardId;

    /**
     * 总价
     */
    @TableField(value = "total_price")
    @ApiModelProperty(value = "总价")
    private BigDecimal totalPrice;

    /**
     * 实收价
     */
    @TableField(value = "actual_price")
    @ApiModelProperty(value = "实收价")
    private BigDecimal actualPrice;

    /**
     * 优惠金额(元)
     */
    @TableField(value = "discount_amount")
    @ApiModelProperty(value = "优惠金额(元)")
    private BigDecimal discountAmount;

    /**
     * 优惠时间(分)
     */
    @TableField(value = "discount_time")
    @ApiModelProperty(value = "优惠时间(分)")
    private Integer discountTime;

    /**
     * 订单状态
     */
    @TableField(value = "order_state")
    @ApiModelProperty(value = "订单状态")
    private OrderStateEnum orderState;

    /**
     * 退款原因
     */
    @TableField(value = "refund_reason")
    @ApiModelProperty(value = "退款原因")
    private String refundReason;

    /**
     * 进场扫码参数
     */
    @TableField(value = "in_scan_param")
    @ApiModelProperty(value = "进场扫码参数")
    private String inScanParam;

    /**
     * 出场扫码参数
     */
    @TableField(value = "out_scan_param")
    @ApiModelProperty(value = "出场扫码参数")
    private String outScanParam;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "逻辑删除字段", hidden = true)
    @JSONField(serialize = false)
    private Integer isDelete;

    /**
     * 支付时间
     */
    @TableField(value = "pay_time")
    @ApiModelProperty(value = "支付时间")
    private Date payTime;

    /**
     * 退款时间
     */
    @TableField(value = "refund_time")
    @ApiModelProperty(value = "退款时间")
    private Date refundTime;

    /**
     * 计费代码
     */
    @TableField(value = "code")
    @ApiModelProperty(value = "计费代码")
    private String code;

    /**
     * 计费开始
     */
    @TableField(value = "start_time")
    @ApiModelProperty(value = "计费开始")
    private Date startTime;

    /**
     * 计费结束
     */
    @TableField(value = "end_time")
    @ApiModelProperty(value = "计费结束")
    private Date endTime;

    /**
     * 停车时长
     */
    @TableField(value = "park_time")
    @ApiModelProperty(value = "停车时长")
    private String parkTime;

    /**
     * 计费时长
     */
    @TableField(value = "charge_time")
    @ApiModelProperty(value = "计费时长")
    private String chargeTime;

    /**
     * 计费标准
     */
    @TableField(value = "charging_car")
    @ApiModelProperty(value = "计费标准")
    private String chargingCar;

    /**
     * 应收金额
     */
    @TableField(value = "total_charge")
    @ApiModelProperty(value = "应收金额")
    private BigDecimal totalCharge;

    /**
     * 已付金额
     */
    @TableField(value = "has_charge")
    @ApiModelProperty(value = "已付金额")
    private BigDecimal hasCharge;

    /**
     * 已含优惠金额
     */
    @TableField(value = "fav_money")
    @ApiModelProperty(value = "已含优惠金额")
    private BigDecimal favMoney;

    /**
     * 已含优惠时间
     */
    @TableField(value = "fav_time")
    @ApiModelProperty(value = "已含优惠时间")
    private Integer favTime;

    /**
     * 当前优惠金额
     */
    @TableField(value = "curr_fav_money")
    @ApiModelProperty(value = "当前优惠金额")
    private BigDecimal currFavMoney;

    /**
     * 当前优惠时间
     */
    @TableField(value = "curr_fav_time")
    @ApiModelProperty(value = "当前优惠时间")
    private Integer currFavTime;

    /**
     * 实收金额
     */
    @TableField(value = "pay_charge")
    @ApiModelProperty(value = "实收金额")
    private BigDecimal payCharge;

    /**
     * 入场时间
     */
    @TableField(value = "in_time")
    @ApiModelProperty(value = "入场时间")
    private Date inTime;

    /**
     * 卡片类型
     */
    @TableField(value = "card_type")
    @ApiModelProperty(value = "卡片类型")
    private String cardType;

    /**
     * 卡片编号
     */
    @TableField(value = "card_sn_id")
    @ApiModelProperty(value = "卡片编号")
    private String cardSnId;

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