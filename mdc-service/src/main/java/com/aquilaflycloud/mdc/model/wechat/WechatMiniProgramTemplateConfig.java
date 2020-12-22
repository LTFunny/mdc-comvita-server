package com.aquilaflycloud.mdc.model.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.wechat.TemplateConfigTypeEnum;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "wechat_mini_program_template_config")
public class WechatMiniProgramTemplateConfig implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 模板配置名称
     */
    @TableField(value = "template_config_name")
    @ApiModelProperty(value = "模板配置名称")
    private String templateConfigName;

    /**
     * 代码模板id
     */
    @TableField(value = "template_id")
    @ApiModelProperty(value = "代码模板id")
    private Long templateId;

    /**
     * 来源小程序appId
     */
    @TableField(value = "source_mini_program_app_id")
    @ApiModelProperty(value = "来源小程序appId")
    private String sourceMiniProgramAppId;

    /**
     * 来源小程序名称
     */
    @TableField(value = "source_mini_program")
    @ApiModelProperty(value = "来源小程序名称")
    private String sourceMiniProgram;

    /**
     * 用户版本号
     */
    @TableField(value = "user_version")
    @ApiModelProperty(value = "用户版本号")
    private String userVersion;

    /**
     * 用户描述
     */
    @TableField(value = "user_desc")
    @ApiModelProperty(value = "用户描述")
    private String userDesc;

    /**
     * 开发者
     */
    @TableField(value = "developer")
    @ApiModelProperty(value = "开发者")
    private String developer;

    /**
     * 模板创建时间
     */
    @TableField(value = "template_time")
    @ApiModelProperty(value = "模板创建时间")
    private Date templateTime;

    /**
     * 小程序默认页面(生成体验码)
     */
    @TableField(value = "default_page")
    @ApiModelProperty(value = "小程序默认页面(生成体验码)")
    private String defaultPage;

    /**
     * 小程序页面配置(多个以分号;区分)
     */
    @TableField(value = "page_config")
    @ApiModelProperty(value = "小程序页面配置(多个以分号;区分)")
    private String pageConfig;

    /**
     * 小程序ext配置
     */
    @TableField(value = "ext_config")
    @ApiModelProperty(value = "小程序ext配置")
    private String extConfig;

    /**
     * 模板类型; 1:联萌小程序
     */
    @TableField(value = "template_type")
    @ApiModelProperty(value = "模板类型; 1:联萌小程序")
    private TemplateConfigTypeEnum templateType;

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
    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /**
     * 子租户id
     */
    @TableField(value = "sub_tenant_id")
    @ApiModelProperty(value = "子租户id")
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