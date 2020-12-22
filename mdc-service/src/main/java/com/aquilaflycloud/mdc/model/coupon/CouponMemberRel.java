package com.aquilaflycloud.mdc.model.coupon;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.common.CreateSourceEnum;
import com.aquilaflycloud.mdc.enums.coupon.*;
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
@TableName(value = "coupon_member_rel")
public class CouponMemberRel implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 优惠券id
     */
    @TableField(value = "coupon_id")
    @ApiModelProperty(value = "优惠券id")
    private Long couponId;

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
     * 优惠券第三方关联id
     */
    @TableField(value = "rel_third_id")
    @ApiModelProperty(value = "优惠券第三方关联id")
    private String relThirdId;

    /**
     * 核销码
     */
    @TableField(value = "verificate_code")
    @ApiModelProperty(value = "核销码")
    private String verificateCode;

    /**
     * 优惠券模式
     */
    @TableField(value = "coupon_mode")
    @ApiModelProperty(value = "优惠券模式")
    private CouponModeEnum couponMode;

    /**
     * 优惠券第三方id
     */
    @TableField(value = "coupon_third_id")
    @ApiModelProperty(value = "优惠券第三方id")
    private String couponThirdId;

    /**
     * 优惠券编码
     */
    @TableField(value = "coupon_code")
    @ApiModelProperty(value = "优惠券编码")
    private String couponCode;

    /**
     * 优惠券名称
     */
    @TableField(value = "coupon_name")
    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    /**
     * 优惠券图片
     */
    @TableField(value = "coupon_img")
    @ApiModelProperty(value = "优惠券图片")
    private String couponImg;

    /**
     * 优惠券类型
     */
    @TableField(value = "coupon_type")
    @ApiModelProperty(value = "优惠券类型")
    private CouponTypeEnum couponType;

    /**
     * 优惠券值
     */
    @TableField(value = "coupon_value")
    @ApiModelProperty(value = "优惠券值")
    private BigDecimal couponValue;

    /**
     * 门槛金额
     */
    @TableField(value = "target_price")
    @ApiModelProperty(value = "门槛金额")
    private BigDecimal targetPrice;

    /**
     * 商品自编码
     */
    @TableField(value = "goods_code")
    @ApiModelProperty(value = "商品自编码")
    private String goodsCode;

    /**
     * 优惠券成本
     */
    @TableField(value = "cost_price")
    @ApiModelProperty(value = "优惠券成本")
    private BigDecimal costPrice;

    /**
     * 领取类型
     */
    @TableField(value = "receive_type")
    @ApiModelProperty(value = "领取类型")
    private ReceiveTypeEnum receiveType;

    /**
     * 可领取开始时间
     */
    @TableField(value = "receive_start_time")
    @ApiModelProperty(value = "可领取开始时间")
    private Date receiveStartTime;

    /**
     * 可领取结束时间
     */
    @TableField(value = "receive_end_time")
    @ApiModelProperty(value = "可领取结束时间")
    private Date receiveEndTime;

    /**
     * 有效类型
     */
    @TableField(value = "effective_type")
    @ApiModelProperty(value = "有效类型")
    private EffectiveTypeEnum effectiveType;

    /**
     * 领取后有效天数
     */
    @TableField(value = "effective_days")
    @ApiModelProperty(value = "领取后有效天数")
    private Integer effectiveDays;

    /**
     * 有效开始时间
     */
    @TableField(value = "effective_start_time")
    @ApiModelProperty(value = "有效开始时间")
    private Date effectiveStartTime;

    /**
     * 有效结束时间
     */
    @TableField(value = "effective_end_time")
    @ApiModelProperty(value = "有效结束时间")
    private Date effectiveEndTime;

    /**
     * 有效核销开始时间
     */
    @TableField(value = "verificate_start_time")
    @ApiModelProperty(value = "有效核销开始时间")
    @JSONField(format = "HH:mm:ss")
    private Date verificateStartTime;

    /**
     * 有效核销结束时间
     */
    @TableField(value = "verificate_end_time")
    @ApiModelProperty(value = "有效核销结束时间")
    @JSONField(format = "HH:mm:ss")
    private Date verificateEndTime;

    /**
     * 优惠券说明
     */
    @TableField(value = "coupon_remark")
    @ApiModelProperty(value = "优惠券说明")
    private String couponRemark;

    /**
     * 领取时间
     */
    @TableField(value = "receive_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "领取时间")
    private Date receiveTime;

    /**
     * 核销方式
     */
    @TableField(value = "verificate_mode")
    @ApiModelProperty(value = "核销方式")
    private VerificateModeEnum verificateMode;

    /**
     * 核销状态
     */
    @TableField(value = "verificate_state")
    @ApiModelProperty(value = "核销状态")
    private VerificateStateEnum verificateState;

    /**
     * 核销时间
     */
    @TableField(value = "verificate_time")
    @ApiModelProperty(value = "核销时间")
    private Date verificateTime;

    /**
     * 核销人id
     */
    @TableField(value = "verificater_id")
    @ApiModelProperty(value = "核销人id", hidden = true)
    @JSONField(serialize = false)
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
    @ApiModelProperty(value = "核销人所属部门ids", hidden = true)
    @JSONField(serialize = false)
    private String verificaterOrgIds;

    /**
     * 核销人所属部门名称
     */
    @TableField(value = "verificater_org_names")
    @ApiModelProperty(value = "核销人所属部门名称")
    private String verificaterOrgNames;

    /**
     * 核销第三方id
     */
    @TableField(value = "verificater_third_id")
    @ApiModelProperty(value = "核销第三方id")
    private String verificaterThirdId;

    /**
     * 核销第三方名称
     */
    @TableField(value = "verificater_third_name")
    @ApiModelProperty(value = "核销第三方名称")
    private String verificaterThirdName;

    /**
     * 消费金额
     */
    @TableField(value = "consume_price")
    @ApiModelProperty(value = "消费金额")
    private BigDecimal consumePrice;

    /**
     * 优惠金额
     */
    @TableField(value = "discount_price")
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountPrice;

    /**
     * 实付金额
     */
    @TableField(value = "actually_price")
    @ApiModelProperty(value = "实付金额")
    private BigDecimal actuallyPrice;

    /**
     * 领取来源id
     */
    @TableField(value = "receive_source_id")
    @ApiModelProperty(value = "领取来源id")
    private Long receiveSourceId;

    /**
     * 领取来源
     */
    @TableField(value = "receive_source")
    @ApiModelProperty(value = "领取来源")
    private ReceiveSourceEnum receiveSource;

    /**
     * 创建来源
     */
    @TableField(value = "create_source", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建来源")
    private CreateSourceEnum createSource;

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

    private static final long serialVersionUID = 1L;
}