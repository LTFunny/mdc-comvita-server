package com.aquilaflycloud.mdc.model.ad;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.ad.*;
import com.aquilaflycloud.mdc.enums.common.AuditStateEnum;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "ad_info")
public class AdInfo implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 微信或支付宝appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    /**
     * 广告名称
     */
    @TableField(value = "ad_name")
    @ApiModelProperty(value = "广告名称")
    private String adName;

    /**
     * 广告类型
     */
    @TableField(value = "ad_type")
    @ApiModelProperty(value = "广告类型")
    private AdTypeEnum adType;

    /**
     * 广告详情
     */
    @TableField(value = "ad_desc")
    @ApiModelProperty(value = "广告详情")
    private String adDesc;

    /**
     * 投放位置
     */
    @TableField(value = "ad_placement")
    @ApiModelProperty(value = "投放位置")
    private AdPlacementEnum adPlacement;

    /**
     * 有效时段起始时间
     */
    @TableField(value = "effective_start_time")
    @ApiModelProperty(value = "有效时段起始时间")
    @JSONField(format = "HH:mm:ss")
    private Date effectiveStartTime;

    /**
     * 有效时段结束时间
     */
    @TableField(value = "effective_end_time")
    @ApiModelProperty(value = "有效时段结束时间")
    @JSONField(format = "HH:mm:ss")
    private Date effectiveEndTime;

    /**
     * 上线时间
     */
    @TableField(value = "online_time")
    @ApiModelProperty(value = "上线时间")
    private Date onlineTime;

    /**
     * 下线时间
     */
    @TableField(value = "offline_time")
    @ApiModelProperty(value = "下线时间")
    private Date offlineTime;

    /**
     * 有效时段类型
     */
    @TableField(value = "effective_period")
    @ApiModelProperty(value = "有效时段类型")
    private EffectivePeriodEnum effectivePeriod;

    /**
     * 广告生效方式
     */
    @TableField(value = "effective_mode")
    @ApiModelProperty(value = "广告生效方式")
    private EffectiveModeEnum effectiveMode;

    /**
     * 图片地址
     */
    @TableField(value = "image_url")
    @ApiModelProperty(value = "图片地址")
    private String imageUrl;

    /**
     * 优先级
     */
    @TableField(value = "priority")
    @ApiModelProperty(value = "优先级")
    private Integer priority;

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
    private AdStateEnum state;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "逻辑删除字段", hidden = true)
    @JSONField(serialize = false)
    private Integer isDelete;

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