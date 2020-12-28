package com.aquilaflycloud.mdc.model.catalog;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.catalog.CatalogBusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.catalog.CatalogHierarchyEnableEnum;
import com.aquilaflycloud.mdc.enums.catalog.CatalogHierarchyTypeEnum;
import com.aquilaflycloud.mdc.enums.catalog.CatalogTypeEnum;
import com.aquilaflycloud.mdc.enums.common.StateEnum;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "catalog_hierarchy_info")
public class CatalogHierarchyInfo implements Serializable {
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @TableField(value = "p_id")
    @ApiModelProperty(value = "父节点id")
    private Long pId;

    @TableField(value = "path")
    @ApiModelProperty(value = "路径")
    private String path;

    @TableField(value = "level")
    @ApiModelProperty(value = "等级")
    private Integer level;

    @TableField(value = "name")
    @ApiModelProperty(value = "名称")
    private String name;

    @TableField(value = "pic_url")
    @ApiModelProperty(value = "图标")
    private String picUrl;

    @TableField(value = "type")
    @ApiModelProperty(value = "类型(catalog.CatalogHierarchyTypeEnum)")
    private CatalogHierarchyTypeEnum type;

    @TableField(value = "sort")
    @ApiModelProperty(value = "排序")
    private Integer sort;

    @TableField(value = "enable")
    @ApiModelProperty(value = "是否启用(catalog.CatalogHierarchyEnableEnum)")
    private CatalogHierarchyEnableEnum enable;

    @TableLogic
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "逻辑删除字段", hidden = true)
    @JSONField(serialize = false)
    private Integer isDelete;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "last_update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后更新时间")
    private Date lastUpdateTime;

    @TableField(value = "tenant_id")
    @ApiModelProperty(value = "租户id", hidden = true)
    @JSONField(serialize = false)
    private Long tenantId;

    @TableField(value = "sub_tenant_id")
    @ApiModelProperty(value = "子租户id", hidden = true)
    @JSONField(serialize = false)
    private Long subTenantId;

    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建记录人id", hidden = true)
    @JSONField(serialize = false)
    private Long creatorId;

    @TableField(value = "creator_name", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建记录人名称")
    private String creatorName;

    @TableField(value = "creator_org_ids", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建用户所属部门ids", hidden = true)
    @JSONField(serialize = false)
    private String creatorOrgIds;

    @TableField(value = "creator_org_names", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建用户所属部门名称")
    private String creatorOrgNames;

    @TableField(value = "last_operator_id", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后操作人id", hidden = true)
    @JSONField(serialize = false)
    private Long lastOperatorId;

    @TableField(value = "last_operator_name", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后操作人名称")
    private String lastOperatorName;

    private static final long serialVersionUID = 1L;
}
