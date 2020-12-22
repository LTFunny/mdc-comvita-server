package com.aquilaflycloud.mdc.model.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信授权账号表
 */
@Data
@TableName(value = "wechat_pay_config")
public class WechatPayConfig implements Serializable {
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
     * 支付商户号
     */
    @TableField(value = "mch_id")
    @ApiModelProperty(value = "支付商户号")
    private String mchId;

    /**
     * 支付商户密钥
     */
    @TableField(value = "mch_key")
    @ApiModelProperty(value = "支付商户密钥")
    private String mchKey;

    /**
     * 子商户appId
     */
    @TableField(value = "sub_app_id")
    @ApiModelProperty(value = "子商户appId")
    private String subAppId;

    /**
     * 子商户号
     */
    @TableField(value = "sub_mch_id")
    @ApiModelProperty(value = "子商户号")
    private String subMchId;

    /**
     * apiclient_cert.p12文件的路径
     */
    @TableField(value = "key_path")
    @ApiModelProperty(value = "apiclient_cert.p12文件的路径")
    private String keyPath;

    /**
     * apiclient_key.pem文件的路径
     */
    @TableField(value = "private_key_path")
    @ApiModelProperty(value = "apiclient_key.pem文件的路径")
    private String privateKeyPath;

    /**
     * apiclient_cert.pem文件的路径
     */
    @TableField(value = "private_cert_path")
    @ApiModelProperty(value = "apiclient_cert.pem文件的路径")
    private String privateCertPath;

    /**
     * 证书序列号
     */
    @TableField(value = "cert_serial_no")
    @ApiModelProperty(value = "证书序列号")
    private String certSerialNo;

    /**
     * apiV3的密钥
     */
    @TableField(value = "api_v3_key")
    @ApiModelProperty(value = "apiV3的密钥")
    private String apiV3Key;

    /**
     * 商圈路径(快速积分)
     */
    @TableField(value = "mall_path")
    @ApiModelProperty(value = "商圈路径(快速积分)")
    private String mallPath;

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
