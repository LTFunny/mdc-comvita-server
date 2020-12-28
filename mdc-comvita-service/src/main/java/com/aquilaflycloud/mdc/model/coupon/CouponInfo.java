package com.aquilaflycloud.mdc.model.coupon;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.common.AuditStateEnum;
import com.aquilaflycloud.mdc.enums.common.CreateSourceEnum;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.coupon.*;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "coupon_info")
public class CouponInfo implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

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
     * 优惠券库存
     */
    @TableField(value = "inventory")
    @ApiModelProperty(value = "优惠券库存")
    private Integer inventory;

    /**
     * 会员领取上限
     */
    @TableField(value = "receive_limit")
    @ApiModelProperty(value = "会员领取上限")
    private Integer receiveLimit;

    /**
     * 累计已领取数量
     */
    @TableField(value = "receive_count")
    @ApiModelProperty(value = "累计已领取数量")
    private Integer receiveCount;

    /**
     * 累计已核销数量
     */
    @TableField(value = "verificate_count")
    @ApiModelProperty(value = "累计已核销数量")
    private Integer verificateCount;

    /**
     * 是否显示
     */
    @TableField(value = "can_show")
    @ApiModelProperty(value = "是否显示")
    private WhetherEnum canShow;

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
     * 发放类型
     */
    @TableField(value = "release_type")
    @ApiModelProperty(value = "发放类型")
    private ReleaseTypeEnum releaseType;

    /**
     * 发放时间
     */
    @TableField(value = "release_time")
    @ApiModelProperty(value = "发放时间")
    private Date releaseTime;

    /**
     * 下架类型
     */
    @TableField(value = "disable_type")
    @ApiModelProperty(value = "下架类型")
    private DisableTypeEnum disableType;

    /**
     * 下架时间
     */
    @TableField(value = "disable_time")
    @ApiModelProperty(value = "下架时间")
    private Date disableTime;

    /**
     * 审核状态
     */
    @TableField(value = "audit_state", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "审核状态")
    private AuditStateEnum auditState;

    /**
     * 审核说明
     */
    @TableField(value = "audit_remark")
    @ApiModelProperty(value = "审核说明")
    private String auditRemark;

    /**
     * 状态
     */
    @TableField(value = "state")
    @ApiModelProperty(value = "状态")
    private CouponStateEnum state;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "逻辑删除字段", hidden = true)
    @JSONField(serialize = false)
    private Integer isDelete;

    /**
     * 创建来源
     */
    @TableField(value = "create_source", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建来源")
    private CreateSourceEnum createSource;

    /**
     * 支付宝pid
     */
    @TableField(value = "alipay_pid")
    @ApiModelProperty(value = "支付宝pid")
    private String alipayPid;

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