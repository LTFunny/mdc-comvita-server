package com.aquilaflycloud.mdc.model.folksonomy;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.folksonomy.FolksonomyTypeEnum;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "folksonomy_business_rel")
public class FolksonomyBusinessRel implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 标签id
     */
    @TableField(value = "folksonomy_id")
    @ApiModelProperty(value = "标签id")
    private Long folksonomyId;

    /**
     * 标签名称
     */
    @TableField(value = "folksonomy_name")
    @ApiModelProperty(value = "标签名称")
    private String folksonomyName;

    /**
     * 标签类型
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "标签类型")
    private FolksonomyTypeEnum type;

    /**
     * 标签目录id
     */
    @TableField(value = "catalog_id")
    @ApiModelProperty(value = "标签目录id")
    private Long catalogId;

    /**
     * 标签目录名称
     */
    @TableField(value = "catalog_name")
    @ApiModelProperty(value = "标签目录名称")
    private String catalogName;

    /**
     * 业务类型
     */
    @TableField(value = "business_type")
    @ApiModelProperty(value = "业务类型")
    private BusinessTypeEnum businessType;

    /**
     * 业务id
     */
    @TableField(value = "business_id")
    @ApiModelProperty(value = "业务id")
    private Long businessId;

    /**
     * 权重
     */
    @TableField(value = "weight")
    @ApiModelProperty(value = "权重")
    private Integer weight;

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
