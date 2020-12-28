package com.aquilaflycloud.mdc.model.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.wechat.ExpireTypeEnum;
import com.aquilaflycloud.mdc.enums.wechat.QrcodeHandlerTypeEnum;
import com.aquilaflycloud.mdc.enums.wechat.QrcodeMsgTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "wechat_author_site_qrcode_msg")
public class WechatAuthorSiteQrcodeMsg implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 二维码持续状态类型
     */
    @TableField(value = "expire_type")
    @ApiModelProperty(value = "二维码持续状态类型")
    private ExpireTypeEnum expireType;

    /**
     * 二维码有效时间，以秒为单位
     */
    @TableField(value = "expire_seconds")
    @ApiModelProperty(value = "二维码有效时间，以秒为单位")
    private Integer expireSeconds;

    /**
     * 自设参数
     */
    @TableField(value = "scene_string")
    @ApiModelProperty(value = "自设参数")
    private String sceneString;

    /**
     * 公众号appid
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value = "公众号appid")
    private String appId;

    /**
     * 创建二维码的ticket
     */
    @TableField(value = "ticket")
    @ApiModelProperty(value = "创建二维码的ticket")
    private String ticket;

    /**
     * 二维码图片解析后的地址
     */
    @TableField(value = "url")
    @ApiModelProperty(value = "二维码图片解析后的地址")
    private String url;

    /**
     * 直接访问二维码图片的url
     */
    @TableField(value = "ticket_url")
    @ApiModelProperty(value = "直接访问二维码图片的url")
    private String ticketUrl;

    /**
     * 访问二维码图片的短url
     */
    @TableField(value = "ticket_short_url")
    @ApiModelProperty(value = "访问二维码图片的短url")
    private String ticketShortUrl;

    /**
     * 逻辑处理类型
     */
    @TableField(value = "handler_type")
    @ApiModelProperty(value = "逻辑处理类型")
    private QrcodeHandlerTypeEnum handlerType;

    /**
     * 逻辑处理内容
     */
    @TableField(value = "handler_content")
    @ApiModelProperty(value = "逻辑处理内容")
    private String handlerContent;

    /**
     * 类型
     */
    @TableField(value = "msg_type")
    @ApiModelProperty(value = "类型")
    private QrcodeMsgTypeEnum msgType;

    /**
     * 回复内容
     */
    @TableField(value = "msg_content")
    @ApiModelProperty(value = "回复内容")
    private String msgContent;

    /**
     * 过期时间
     */
    @TableField(value = "expire_time")
    @ApiModelProperty(value = "过期时间")
    private Date expireTime;

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