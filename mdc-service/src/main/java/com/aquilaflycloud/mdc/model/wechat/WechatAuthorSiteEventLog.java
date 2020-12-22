package com.aquilaflycloud.mdc.model.wechat;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "wechat_author_site_event_log")
public class WechatAuthorSiteEventLog implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 微信appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value = "微信appId")
    private String appId;

    /**
     * 微信openId
     */
    @TableField(value = "open_id")
    @ApiModelProperty(value = "微信openId")
    private String openId;

    /**
     * 事件类型
     */
    @TableField(value = "event")
    @ApiModelProperty(value = "事件类型")
    private String event;

    /**
     * 消息类型
     */
    @TableField(value = "msg_type")
    @ApiModelProperty(value = "消息类型")
    private String msgType;

    /**
     * 微信事件json串
     */
    @TableField(value = "event_content")
    @ApiModelProperty(value = "微信事件json串")
    private String eventContent;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 事件发生时间
     */
    @TableField(value = "event_time")
    @ApiModelProperty(value = "事件发生时间")
    private Date eventTime;

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

    private static final long serialVersionUID = 1L;
}