package com.aquilaflycloud.mdc.model.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "wechat_public_event_log")
public class WechatPublicEventLog implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 微信openId
     */
    @TableField(value = "open_id")
    @ApiModelProperty(value = "微信openId")
    private String openId;

    /**
     * 微信appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value = "微信appId")
    private String appId;

    /**
     * 事件类型
     */
    @TableField(value = "event")
    @ApiModelProperty(value = "事件类型")
    private String event;

    /**
     * 事件key值
     */
    @TableField(value = "event_key")
    @ApiModelProperty(value = "事件key值")
    private String eventKey;

    /**
     * 消息类型
     */
    @TableField(value = "msg_type")
    @ApiModelProperty(value = "消息类型")
    private String msgType;

    /**
     * 粉丝回复消息内容
     */
    @TableField(value = "msg_content")
    @ApiModelProperty(value = "粉丝回复消息内容")
    private String msgContent;

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
     * 微信事件json串
     */
    @TableField(value = "event_content")
    @ApiModelProperty(value = "微信事件json串")
    private String eventContent;

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

    private static final long serialVersionUID = 1L;
}