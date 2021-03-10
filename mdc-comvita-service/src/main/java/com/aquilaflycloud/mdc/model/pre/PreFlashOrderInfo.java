package com.aquilaflycloud.mdc.model.pre;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.pre.FlashOrderInfoStateEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 快闪订单信息表
 */
@Data
@TableName(value = "pre_flash_order_info")
public class PreFlashOrderInfo implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 活动id
     */
    @TableField(value = "activity_info_id")
    @ApiModelProperty(value = "活动id")
    private Long activityInfoId;
    /**
     * 会员id
     */
    @TableField(value = "member_id")
    @ApiModelProperty(value = "会员id")
    private Long memberId;
    /**
     * 门店id
     */
    @TableField(value = "shop_id")
    @ApiModelProperty(value = "门店id")
    private Long shopId;
    /**
     * 订单编码
     */
    @TableField(value = "flash_code")
    @ApiModelProperty(value = "核销码")
    private String flashCode;
    /**
     * 买家姓名
     */
    @TableField(value = "buyer_name")
    @ApiModelProperty(value = "领取人")
    private String buyerName;
    /**
     * 门店名称
     */
    @TableField(value = "shop_name")
    @ApiModelProperty(value = "参加渠道")
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
     * 快递单号
     */
    @TableField(value = "express_order")
    @ApiModelProperty(value = "快递单号")
    private String expressOrder;

    /**
     * 订单状态
     */
    @TableField(value = "flash_order_state")
    @ApiModelProperty(value = "状态")
    private FlashOrderInfoStateEnum flashOrderState;

    /**
     * 活动开始时间
     */
    @TableField(value = "begin_time")
    @ApiModelProperty(value = "活动开始时间")
    private Date beginTime;

    /**
     * 活动结束时间
     */
    @TableField(value = "end_time")
    @ApiModelProperty(value = "活动结束时间")
    private Date endTime;

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
