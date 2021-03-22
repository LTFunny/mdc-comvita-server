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
import java.util.Date;

/**
 * 点评信息
 * @author linkq
 */
@Data
@TableName(value = "pre_comment")
public class PreCommentInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 点评内容
     */
    @TableField(value = "com_content")
    @ApiModelProperty(value = "点评内容")
    private String comContent;

    /**
     * 点评图片
     */
    @TableField(value = "com_picture")
    @ApiModelProperty(value = "点评图片")
    private String comPicture;

    /**
     * 活动id
     */
    @TableField(value = "activity_id")
    @ApiModelProperty(value = "活动id")
    private Long activityId;

    /**
     * 活动名称
     */
    @TableField(value = "activity_name")
    @ApiModelProperty(value = "活动名称")
    private String activityName;

    /**
     * 状态 1-待审核 2-审核通过 3-审核不通过
     */
    @TableField(value = "com_state")
    @ApiModelProperty(value = "点评状态(pre.ActivityCommentStateEnum)")
    private ActivityCommentStateEnum comState;

    /**
     * 展示状态 1-隐藏 2公开
     */
    @TableField(value = "com_view_state")
    @ApiModelProperty(value = "展示状态（pre.ActivityCommentViewStateEnum）")
    private ActivityCommentViewStateEnum comViewState;

    /**
     * 点评人
     */
    @TableField(value = "commentator")
    @ApiModelProperty(value = "点评人")
    private String commentator;

    /**
     * 点评人Id
     */
    @TableField(value = "commentator_id")
    @ApiModelProperty(value = "点评人Id")
    private Long commentatorId;

    /**
     * 审核人
     */
    @TableField(value = "auditor")
    @ApiModelProperty(value = "审核人")
    private String auditor;

    /**
     * 审核人Id
     */
    @TableField(value = "auditor_id")
    @ApiModelProperty(value = "审核人Id")
    private Long auditorId;

    /**
     * 审核备注
     */
    @TableField(value = "audit_remark")
    @ApiModelProperty(value = "审核备注")
    private String auditRemark;

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