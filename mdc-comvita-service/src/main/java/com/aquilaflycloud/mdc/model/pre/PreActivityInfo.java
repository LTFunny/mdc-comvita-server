package com.aquilaflycloud.mdc.model.pre;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.pre.ActivityStateEnum;
import com.aquilaflycloud.mdc.enums.pre.ActivityTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动信息
 */
@Data
@TableName(value = "pre_activity_info")
public class PreActivityInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 活动名称
     */
    @TableField(value = "activity_name")
    @ApiModelProperty(value = "活动名称")
    private String activityName;

    /**
     * 活动类型
     */
    @TableField(value = "activity_type")
    @ApiModelProperty(value = "活动类型(pre.ActivityTypeEnum)")
    private ActivityTypeEnum activityType;

    /**
     * 状态（1-未开始 2-进行中 3-已结束 4-已下架）
     */
    @TableField(value = "activity_state")
    @ApiModelProperty(value = "状态（pre.ActivityStateEnum）")
    private ActivityStateEnum activityState;

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
     * 关联商品
     */
    @TableField(value = "ref_goods")
    @ApiModelProperty(value = "关联商品")
    private Long refGoods;

    /**
     * 关联的销售规则
     */
    @TableField(value = "ref_rule")
    @ApiModelProperty(value = "关联的销售规则")
    private Long refRule;

    /**
     * 奖励规则
     */
    @TableField(value = "reward_rule_content")
    @ApiModelProperty(value = "奖励规则", hidden = true)
    @JSONField(serialize = false)
    private String rewardRuleContent;

    /**
     * 活动照片
     */
    @TableField(value = "activity_picture")
    @ApiModelProperty(value = "活动照片")
    private String activityPicture;

    /**
     * 活动介绍
     */
    @TableField(value = "activity_profile")
    @ApiModelProperty(value = "活动介绍")
    private String activityProfile;

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
