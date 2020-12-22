package com.aquilaflycloud.mdc.model.system;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.system.ExportStateEnum;
import com.aquilaflycloud.mdc.enums.system.ExportTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "system_export_log")
public class SystemExportLog implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 导出类型(业务功能类型)
     */
    @TableField(value = "export_type")
    @ApiModelProperty(value = "导出类型(业务功能类型)")
    private ExportTypeEnum exportType;

    /**
     * 导出条件内容
     */
    @TableField(value = "export_content")
    @ApiModelProperty(value = "导出条件内容")
    private String exportContent;

    /**
     * oss的bucketName
     */
    @TableField(value = "bucket_name")
    @ApiModelProperty(value = "oss的bucketName")
    private String bucketName;

    /**
     * 上传目录
     */
    @TableField(value = "path")
    @ApiModelProperty(value = "上传目录")
    private String path;

    /**
     * 上传文件名
     */
    @TableField(value = "file_name")
    @ApiModelProperty(value = "上传文件名")
    private String fileName;

    /**
     * 上传文件后缀
     */
    @TableField(value = "ext")
    @ApiModelProperty(value = "上传文件后缀")
    private String ext;

    /**
     * 上传文件完整路径
     */
    @TableField(value = "object_key")
    @ApiModelProperty(value = "上传文件完整路径")
    private String objectKey;

    /**
     * 文件下载路径
     */
    @TableField(value = "url")
    @ApiModelProperty(value = "文件下载路径")
    private String url;

    /**
     * 任务状态
     */
    @TableField(value = "state")
    @ApiModelProperty(value = "任务状态")
    private ExportStateEnum state;

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