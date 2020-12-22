package com.aquilaflycloud.mdc.model.parking;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.common.CreateSourceEnum;
import com.aquilaflycloud.mdc.enums.parking.*;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "parking_coupon")
public class ParkingCoupon implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 停车券编码
     */
    @TableField(value = "coupon_code")
    @ApiModelProperty(value = "停车券编码")
    private String couponCode;

    /**
     * 停车券名称
     */
    @TableField(value = "coupon_name")
    @ApiModelProperty(value = "停车券名称")
    private String couponName;

    /**
     * 优惠类型
     */
    @TableField(value = "coupon_type")
    @ApiModelProperty(value = "优惠类型")
    private CouponTypeEnum couponType;

    /**
     * 优惠值
     */
    @TableField(value = "coupon_worth")
    @ApiModelProperty(value = "优惠值")
    private BigDecimal couponWorth;

    /**
     * 优惠券使用说明
     */
    @TableField(value = "coupon_remark")
    @ApiModelProperty(value = "优惠券使用说明")
    private String couponRemark;

    /**
     * 有效类型
     */
    @TableField(value = "effective_type")
    @ApiModelProperty(value = "有效类型")
    private EffectiveTypeEnum effectiveType;

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
     * 库存类型
     */
    @TableField(value = "inventory_type")
    @ApiModelProperty(value = "库存类型")
    private InventoryTypeEnum inventoryType;

    /**
     * 库存值
     */
    @TableField(value = "inventory")
    @ApiModelProperty(value = "库存值")
    private BigDecimal inventory;

    /**
     * 派发值
     */
    @TableField(value = "distribute")
    @ApiModelProperty(value = "派发值")
    private BigDecimal distribute;

    /**
     * 领取限制类型
     */
    @TableField(value = "receive_limit_type")
    @ApiModelProperty(value = "领取限制类型")
    private LimitTypeEnum receiveLimitType;

    /**
     * 领取限制内容
     */
    @TableField(value = "receive_limit_content")
    @ApiModelProperty(value = "领取限制内容")
    private String receiveLimitContent;

    /**
     * 使用限制类型
     */
    @TableField(value = "use_limit_type")
    @ApiModelProperty(value = "使用限制类型")
    private LimitTypeEnum useLimitType;

    /**
     * 使用限制内容
     */
    @TableField(value = "use_limit_content")
    @ApiModelProperty(value = "使用限制内容")
    private String useLimitContent;

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