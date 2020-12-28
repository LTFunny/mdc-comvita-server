package com.aquilaflycloud.mdc.model.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "wechat_open_platform")
public class WechatOpenPlatform implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 微信第三方平台appId
     */
    @TableField(value = "component_appid")
    @ApiModelProperty(value = "微信第三方平台appId")
    private String componentAppid;

    /**
     * 微信第三方平台appsecret
     */
    @TableField(value = "component_appsecret")
    @ApiModelProperty(value = "微信第三方平台appsecret")
    private String componentAppsecret;

    /**
     * 微信第三方安全ticket
     */
    @TableField(value = "component_verify_ticket")
    @ApiModelProperty(value = "微信第三方安全ticket")
    private String componentVerifyTicket;

    /**
     * 微信第三方平台接口调用凭证
     */
    @TableField(value = "component_access_token")
    @ApiModelProperty(value = "微信第三方平台接口调用凭证")
    private String componentAccessToken;

    /**
     * 加密串
     */
    @TableField(value = "encoding_aes_key")
    @ApiModelProperty(value = "加密串")
    private String encodingAesKey;

    /**
     * 验证token
     */
    @TableField(value = "verify_token")
    @ApiModelProperty(value = "验证token")
    private String verifyToken;

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

    private static final long serialVersionUID = 1L;
}