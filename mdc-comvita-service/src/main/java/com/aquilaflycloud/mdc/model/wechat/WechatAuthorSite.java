package com.aquilaflycloud.mdc.model.wechat;

import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.wechat.*;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "wechat_author_site")
public class WechatAuthorSite implements Serializable {
    /**
     * 主键ID
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 授权归属的第三方平台appid
     */
    @TableField(value = "component_appid")
    @ApiModelProperty(value = "授权归属的第三方平台appid")
    private String componentAppid;

    /**
     * 授权类型,1:公众号;2:小程序
     */
    @TableField(value = "source")
    @ApiModelProperty(value = "授权类型,1:公众号;2:小程序")
    private SiteSourceEnum source;

    /**
     * 微信授权来源类型，1:商场，2:商户
     */
    @TableField(value = "site_type")
    @ApiModelProperty(value = "微信授权来源类型，1:商场，2:商户")
    private SiteSiteTypeEnum siteType;

    /**
     * 授权方appid
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value = "授权方appid")
    private String appId;

    /**
     * 授权方接口调用凭据
     */
    @TableField(value = "access_token")
    @ApiModelProperty(value = "授权方接口调用凭据")
    private String accessToken;

    /**
     * 授权方接口调用凭据刷新令牌，刷新令牌主要用于第三方平台获取和刷新已授权用户的access_token，只会在授权时刻提供，请妥善保存。 一旦丢失，只能让用户重新授权，才能再次拿到新的刷新令牌
     */
    @TableField(value = "refresh_token")
    @ApiModelProperty(value = "授权方接口调用凭据刷新令牌，刷新令牌主要用于第三方平台获取和刷新已授权用户的access_token，只会在授权时刻提供，请妥善保存。 一旦丢失，只能让用户重新授权，才能再次拿到新的刷新令牌")
    private String refreshToken;

    /**
     * 授权方昵称
     */
    @TableField(value = "nick_name")
    @ApiModelProperty(value = "授权方昵称")
    private String nickName;

    /**
     * 授权方头像
     */
    @TableField(value = "head_img")
    @ApiModelProperty(value = "授权方头像")
    private String headImg;

    /**
     * 授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号
     */
    @TableField(value = "service_type_info")
    @ApiModelProperty(value = "授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号")
    private SiteServiceTypeEnum serviceTypeInfo;

    /**
     * 授权方认证类型，-1代表未认证，0代表微信认证
     */
    @TableField(value = "verify_type_info")
    @ApiModelProperty(value = "授权方认证类型，-1代表未认证，0代表微信认证")
    private SiteVerifyTypeEnum verifyTypeInfo;

    /**
     * 授权方原始ID
     */
    @TableField(value = "user_name")
    @ApiModelProperty(value = "授权方原始ID")
    private String userName;

    /**
     * 帐号介绍
     */
    @TableField(value = "signature")
    @ApiModelProperty(value = "帐号介绍")
    private String signature;

    /**
     * 授权方主体名称
     */
    @TableField(value = "principal_name")
    @ApiModelProperty(value = "授权方主体名称")
    private String principalName;

    /**
     * 授权方公众号所设置的微信号，可能为空
     */
    @TableField(value = "alias")
    @ApiModelProperty(value = "授权方公众号所设置的微信号，可能为空")
    private String alias;

    /**
     * 授权方功能开通状况：用以了解以下功能的开通状况（0代表未开通，1代表已开通）：open_store:是否开通微信门店功能open_scan:是否开通微信扫商品功能open_pay:是否开通微信支付功能open_card:是否开通微信卡券功能open_shake:是否开通微信摇一摇功能
     */
    @TableField(value = "business_info")
    @ApiModelProperty(value = "授权方功能开通状况：用以了解以下功能的开通状况（0代表未开通，1代表已开通）：open_store:是否开通微信门店功能open_scan:是否开通微信扫商品功能open_pay:是否开通微信支付功能open_card:是否开通微信卡券功能open_shake:是否开通微信摇一摇功能")
    private String businessInfo;

    /**
     * 二维码图片URL
     */
    @TableField(value = "qrcode_url")
    @ApiModelProperty(value = "二维码图片URL")
    private String qrcodeUrl;

    /**
     * 授权方权限集列表:ID为1到15时分别代表：1:消息管理权限2:用户管理权限3:帐号服务权限4:网页服务权限5:微信小店权限6:微信多客服权限7:群发与通知权限8:微信卡券权限9:微信扫一扫权限10:微信连WIFI权限11:素材管理权限12:微信摇周边权限13:微信门店权限14:微信支付权限15:自定义菜单权限17.帐号管理权限18.开发管理权限19.客服消息管理权限
     */
    @TableField(value = "func_info")
    @ApiModelProperty(value = "授权方权限集列表:ID为1到15时分别代表：1:消息管理权限2:用户管理权限3:帐号服务权限4:网页服务权限5:微信小店权限6:微信多客服权限7:群发与通知权限8:微信卡券权限9:微信扫一扫权限10:微信连WIFI权限11:素材管理权限12:微信摇周边权限13:微信门店权限14:微信支付权限15:自定义菜单权限17.帐号管理权限18.开发管理权限19.客服消息管理权限")
    private String funcInfo;

    /**
     * 是否为小程序授权的标识
     */
    @TableField(value = "miniprograminfo")
    @ApiModelProperty(value = "是否为小程序授权的标识")
    private String miniprograminfo;

    /**
     * 预授权码
     */
    @TableField(value = "authorization_code")
    @ApiModelProperty(value = "预授权码")
    private String authorizationCode;

    /**
     * JS-SDK权限验证的签名
     */
    @TableField(value = "jsapi_ticket")
    @ApiModelProperty(value = "JS-SDK权限验证的签名")
    private String jsapiTicket;

    /**
     * 授权状态，1:授权，0:取消授权
     */
    @TableField(value = "state")
    @ApiModelProperty(value = "授权状态")
    private SiteStateEnum state;

    /**
     * 小程序的Secret
     */
    @TableField(value = "secret")
    @ApiModelProperty(value = "小程序的Secret")
    private String secret;

    /**
     * 小程序消息服务器配置的token
     */
    @TableField(value = "token")
    @ApiModelProperty(value = "小程序消息服务器配置的token")
    private String token;

    /**
     * 小程序消息服务器配置的EncodingAESKey
     */
    @TableField(value = "aes_key")
    @ApiModelProperty(value = "小程序消息服务器配置的EncodingAESKey")
    private String aesKey;

    /**
     * 小程序消息格式,XML或者JSON
     */
    @TableField(value = "msg_data_format")
    @ApiModelProperty(value = "小程序消息格式,XML或者JSON")
    private String msgDataFormat;

    /**
     * 是否第三方代理
     */
    @TableField(value = "is_agent")
    @ApiModelProperty(value="是否第三方代理")
    private WhetherEnum isAgent;

    /**
     * 是否显示
     */
    @TableField(value = "is_show")
    @ApiModelProperty(value="是否显示")
    private WhetherEnum isShow;

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