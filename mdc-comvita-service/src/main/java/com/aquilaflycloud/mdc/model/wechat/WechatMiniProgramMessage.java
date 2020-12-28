package com.aquilaflycloud.mdc.model.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.common.StateEnum;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageLangEnum;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageStateEnum;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageTmplTypeEnum;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "wechat_mini_program_message")
public class WechatMiniProgramMessage implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 微信小程序appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value = "微信小程序appId")
    private String appId;

    /**
     * 消息类型
     */
    @TableField(value = "message_type")
    @ApiModelProperty(value = "消息类型")
    private MiniMessageTypeEnum messageType;

    /**
     * 小程序模板id
     */
    @TableField(value = "pri_tmpl_id")
    @ApiModelProperty(value = "小程序模板id")
    private String priTmplId;

    /**
     * 模板标题
     */
    @TableField(value = "title")
    @ApiModelProperty(value = "模板标题")
    private String title;

    /**
     * 模板内容
     */
    @TableField(value = "content")
    @ApiModelProperty(value = "模板内容")
    private String content;

    /**
     * 模板内容示例
     */
    @TableField(value = "example")
    @ApiModelProperty(value = "模板内容示例")
    private String example;

    /**
     * 模板类型，2 代表一次性订阅，3 代表长期订阅
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "模板类型，2 代表一次性订阅，3 代表长期订阅")
    private MiniMessageTmplTypeEnum type;

    /**
     * 小程序页面路径
     */
    @TableField(value = "page_path")
    @ApiModelProperty(value = "小程序页面路径")
    private String pagePath;

    /**
     * 小程序版本
     */
    @TableField(value = "mini_state")
    @ApiModelProperty(value = "小程序版本")
    private MiniMessageStateEnum miniState;

    /**
     * 进入小程序查看的语言类型
     */
    @TableField(value = "mini_lang")
    @ApiModelProperty(value = "进入小程序查看的语言类型")
    private MiniMessageLangEnum miniLang;

    /**
     * 模板内容参数名称
     */
    @TableField(value = "param_name")
    @ApiModelProperty(value = "模板内容参数名称")
    private String paramName;

    /**
     * 状态
     */
    @TableField(value = "state", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "状态")
    private StateEnum state;

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