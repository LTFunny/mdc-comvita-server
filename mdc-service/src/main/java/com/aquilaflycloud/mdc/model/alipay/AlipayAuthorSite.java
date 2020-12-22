package com.aquilaflycloud.mdc.model.alipay;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.alipay.SiteSourceEnum;
import com.aquilaflycloud.mdc.enums.alipay.SiteStatusEnum;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.wechat.SiteStateEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "alipay_author_site")
public class AlipayAuthorSite implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 授权归属的第三方平台appid
     */
    @TableField(value = "component_appid")
    @ApiModelProperty(value="授权归属的第三方平台appid")
    private String componentAppid;

    /**
     * 授权方appid
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value="授权方appid")
    private String appId;

    /**
     * 应用名
     */
    @TableField(value = "app_name")
    @ApiModelProperty(value="应用名")
    private String appName;

    /**
     * 应用信息内容
     */
    @TableField(value = "app_content")
    @ApiModelProperty(value="应用信息内容")
    private String appContent;

    /**
     * 授权类型
     */
    @TableField(value = "source")
    @ApiModelProperty(value="授权类型")
    private SiteSourceEnum source;

    /**
     * 授权方接口调用凭据
     */
    @TableField(value = "auth_token")
    @ApiModelProperty(value="授权方接口调用凭据")
    private String authToken;

    /**
     * 授权方接口调用凭据刷新令牌，刷新令牌主要用于第三方平台获取和刷新已授权用户的access_token，只会在授权时刻提供，请妥善保存。 一旦丢失，只能让用户重新授权，才能再次拿到新的刷新令牌
     */
    @TableField(value = "refresh_token")
    @ApiModelProperty(value="授权方接口调用凭据刷新令牌，刷新令牌主要用于第三方平台获取和刷新已授权用户的access_token，只会在授权时刻提供，请妥善保存。 一旦丢失，只能让用户重新授权，才能再次拿到新的刷新令牌")
    private String refreshToken;

    /**
     * 授权者的 PID
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="授权者的 PID")
    private String userId;

    /**
     * 授权方接口列表
     */
    @TableField(value = "auth_methods")
    @ApiModelProperty(value="授权方接口列表")
    private String authMethods;

    /**
     * 授权方授权生效时间
     */
    @TableField(value = "auth_start_time")
    @ApiModelProperty(value="授权方授权生效时间")
    private Date authStartTime;

    /**
     * 授权方授权失效时间
     */
    @TableField(value = "auth_end_time")
    @ApiModelProperty(value="授权方授权失效时间")
    private Date authEndTime;

    /**
     * 授权方状态
     */
    @TableField(value = "status")
    @ApiModelProperty(value="授权方状态")
    private SiteStatusEnum status;

    /**
     * 是否第三方代理
     */
    @TableField(value = "is_agent")
    @ApiModelProperty(value="是否第三方代理")
    private WhetherEnum isAgent;

    /**
     * 商户密钥
     */
    @TableField(value = "merchant_private_key")
    @ApiModelProperty(value="商户密钥")
    private String merchantPrivateKey;

    /**
     * 支付宝公钥
     */
    @TableField(value = "alipay_public_key")
    @ApiModelProperty(value="支付宝公钥")
    private String alipayPublicKey;

    /**
     * 加密串
     */
    @TableField(value = "encoding_aes_key")
    @ApiModelProperty(value = "加密串")
    private String encodingAesKey;

    /**
     * 授权状态
     */
    @TableField(value = "state")
    @ApiModelProperty(value="授权状态")
    private SiteStateEnum state;

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

    /**
     * 友盟appkey
     */
    @TableField(value = "umeng_app_key")
    @ApiModelProperty(value = "友盟appkey", hidden = true)
    private String umengAppKey;

    /**
     * 友盟apiKey
     */
    @TableField(value = "umeng_api_key")
    @ApiModelProperty(value = "友盟apiKey", hidden = true)
    private String umengApiKey;

    /**
     * 友盟apiSecurity
     */
    @TableField(value = "umeng_api_sec_key")
    @ApiModelProperty(value = "友盟apiSecurity", hidden = true)
    private String umengApiSecKey;


    private static final long serialVersionUID = 1L;
}