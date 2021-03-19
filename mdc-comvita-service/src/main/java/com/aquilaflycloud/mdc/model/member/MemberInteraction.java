package com.aquilaflycloud.mdc.model.member;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.member.InteractionBusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.InteractionTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员互动记录表
 */
@Data
@TableName(value = "member_interaction")
public class MemberInteraction implements Serializable {
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
     * 业务类型
     */
    @TableField(value = "business_type")
    @ApiModelProperty(value = "业务类型")
    private InteractionBusinessTypeEnum businessType;

    /**
     * 业务id
     */
    @TableField(value = "business_id")
    @ApiModelProperty(value = "业务id")
    private Long businessId;

    /**
     * 互动类型
     */
    @TableField(value = "interaction_type")
    @ApiModelProperty(value = "互动类型")
    private InteractionTypeEnum interactionType;

    /**
     * 互动发生时间
     */
    @TableField(value = "interaction_time")
    @ApiModelProperty(value = "互动发生时间")
    private Date interactionTime;

    /**
     * 是否取消
     */
    @TableField(value = "is_cancel")
    @ApiModelProperty(value = "互动状态")
    private WhetherEnum isCancel;

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
