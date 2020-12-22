package com.aquilaflycloud.mdc.model.alipay;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "alipay_open_platform")
public class AlipayOpenPlatform implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 支付宝第三方平台appId
     */
    @TableField(value = "component_appid")
    @ApiModelProperty(value = "支付宝第三方平台appId")
    private String componentAppid;

    /**
     * 支付宝第三方平台网关地址
     */
    @TableField(value = "component_gateway_url")
    @ApiModelProperty(value = "支付宝第三方平台网关地址")
    private String componentGatewayUrl;

    /**
     * 支付宝第三方私钥
     */
    @TableField(value = "component_private_key")
    @ApiModelProperty(value = "支付宝第三方私钥")
    private String componentPrivateKey;

    /**
     * 支付宝平台公钥
     */
    @TableField(value = "component_public_key")
    @ApiModelProperty(value = "支付宝平台公钥")
    private String componentPublicKey;

    /**
     * 加密串
     */
    @TableField(value = "encoding_aes_key")
    @ApiModelProperty(value = "加密串")
    private String encodingAesKey;

    /**
     * 请求方式
     */
    @TableField(value = "format_type")
    @ApiModelProperty(value = "请求方式")
    private String formatType;

    /**
     * 签名方式
     */
    @TableField(value = "sign_type")
    @ApiModelProperty(value = "签名方式")
    private String signType;

    /**
     * 蚂蚁金服服务端地址,用户消息传递
     */
    @TableField(value = "server_host")
    @ApiModelProperty(value = "蚂蚁金服服务端地址,用户消息传递")
    private String serverHost;

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