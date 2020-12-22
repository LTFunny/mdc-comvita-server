package com.aquilaflycloud.mdc.model.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.wechat.CodeAuditStateEnum;
import com.aquilaflycloud.mdc.enums.wechat.CodeReleaseStateEnum;
import com.aquilaflycloud.mdc.enums.wechat.CodeVisitEnum;
import com.aquilaflycloud.mdc.enums.wechat.TemplateConfigTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "wechat_mini_program_code")
public class WechatMiniProgramCode implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 小程序appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value = "小程序appId")
    private String appId;

    /**
     * 模板配置id
     */
    @TableField(value = "template_config_id")
    @ApiModelProperty(value = "模板配置id")
    private Long templateConfigId;

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
     * 用户版本号(模板配置)
     */
    @TableField(value = "user_version")
    @ApiModelProperty(value = "用户版本号(模板配置)")
    private String userVersion;

    /**
     * 用户描述(模板配置)
     */
    @TableField(value = "user_desc")
    @ApiModelProperty(value = "用户描述(模板配置)")
    private String userDesc;

    /**
     * 开发者
     */
    @TableField(value = "developer")
    @ApiModelProperty(value = "开发者")
    private String developer;

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
    @ApiModelProperty(value = "模板类型")
    private TemplateConfigTypeEnum templateType;

    /**
     * 代码版本号
     */
    @TableField(value = "code_version")
    @ApiModelProperty(value = "代码版本号")
    private String codeVersion;

    /**
     * 代码描述
     */
    @TableField(value = "code_desc")
    @ApiModelProperty(value = "代码描述")
    private String codeDesc;

    /**
     * 体验版二维码链接
     */
    @TableField(value = "qrcode_url")
    @ApiModelProperty(value = "体验版二维码链接")
    private String qrcodeUrl;

    /**
     * 提交代码时间
     */
    @TableField(value = "submit_time")
    @ApiModelProperty(value = "提交代码时间")
    private Date submitTime;

    /**
     * 提交审核时间
     */
    @TableField(value = "submit_audit_time")
    @ApiModelProperty(value = "提交审核时间")
    private Date submitAuditTime;

    /**
     * 提交审核内容
     */
    @TableField(value = "audit_content")
    @ApiModelProperty(value = "提交审核内容")
    private String auditContent;

    /**
     * 审核编号
     */
    @TableField(value = "audit_id")
    @ApiModelProperty(value = "审核编号")
    private Long auditId;

    /**
     * 审核状态;1:审核中,2:审核成功,3:审核失败,4:已撤回
     */
    @TableField(value = "audit_state")
    @ApiModelProperty(value = "审核状态")
    private CodeAuditStateEnum auditState;

    /**
     * 审核成功时间
     */
    @TableField(value = "success_audit_time")
    @ApiModelProperty(value = "审核成功时间")
    private Date successAuditTime;

    /**
     * 审核失败时间
     */
    @TableField(value = "failed_audit_time")
    @ApiModelProperty(value = "审核失败时间")
    private Date failedAuditTime;

    /**
     * 审核失败原因
     */
    @TableField(value = "failed_reason")
    @ApiModelProperty(value = "审核失败原因")
    private String failedReason;

    /**
     * 审核失败的小程序截图示例(mediaId,|分隔)
     */
    @TableField(value = "failed_screen_shot")
    @ApiModelProperty(value = "审核失败的小程序截图示例(mediaId,|分隔)")
    private String failedScreenShot;

    /**
     * 发布状态;1:已发布,2:已回退,3:灰度发布
     */
    @TableField(value = "release_state")
    @ApiModelProperty(value = "发布状态")
    private CodeReleaseStateEnum releaseState;

    /**
     * 发布时间
     */
    @TableField(value = "release_time")
    @ApiModelProperty(value = "发布时间")
    private Date releaseTime;

    /**
     * 线上版是否可访问;1:是,0:否
     */
    @TableField(value = "is_visit")
    @ApiModelProperty(value = "线上版是否可访问")
    private CodeVisitEnum isVisit;

    /**
     * 灰度发布计划内容
     */
    @TableField(value = "gray_release_plan")
    @ApiModelProperty(value = "灰度发布计划内容")
    private String grayReleasePlan;

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