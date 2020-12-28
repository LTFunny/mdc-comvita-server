package com.aquilaflycloud.mdc.model.shop;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "shop_category_info")
public class ShopCategoryInfo implements Serializable {

    private static final long serialVersionUID = -271518068355084799L;

    @ApiModelProperty(value = "主键id")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "描述")
    @TableField("simple_desc")
    private String simpleDesc;

    @ApiModelProperty(value = "排序")
    @TableField("catalog_order")
    private Integer catalogOrder;

    @TableLogic
    @ApiModelProperty(value = "逻辑删除字段", hidden = true)
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
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

    @TableField(value = "designate_org_ids")
    @ApiModelProperty(value = "指定用户部门ids")
    private String designateOrgIds;

    @TableField(value = "designate_org_names")
    @ApiModelProperty(value = "指定用户部门名称")
    private String designateOrgNames;
}
