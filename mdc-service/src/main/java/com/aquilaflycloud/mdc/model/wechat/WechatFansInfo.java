package com.aquilaflycloud.mdc.model.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.member.SexEnum;
import com.aquilaflycloud.mdc.enums.member.SourceEnum;
import com.aquilaflycloud.mdc.enums.member.SubscribeSceneEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "wechat_fans_info")
public class WechatFansInfo implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 是否授权
     */
    @TableField(value = "is_auth")
    @ApiModelProperty(value = "是否授权")
    private WhetherEnum isAuth;

    /**
     * 授权时间
     */
    @TableField(value = "auth_time")
    @ApiModelProperty(value = "授权时间")
    private Date authTime;

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
     * 微信unionId
     */
    @TableField(value = "union_id")
    @ApiModelProperty(value = "微信unionId")
    private String unionId;

    /**
     * 微信昵称
     */
    @TableField(value = "nick_name")
    @ApiModelProperty(value = "微信昵称")
    private String nickName;

    /**
     * 微信性别
     */
    @TableField(value = "gender")
    @ApiModelProperty(value = "微信性别")
    private SexEnum gender;

    /**
     * 微信国家
     */
    @TableField(value = "country")
    @ApiModelProperty(value = "微信国家")
    private String country;

    /**
     * 微信省份
     */
    @TableField(value = "province")
    @ApiModelProperty(value = "微信省份")
    private String province;

    /**
     * 微信城市
     */
    @TableField(value = "city")
    @ApiModelProperty(value = "微信城市")
    private String city;

    /**
     * 微信头像
     */
    @TableField(value = "avatar_url")
    @ApiModelProperty(value = "微信头像")
    private String avatarUrl;

    /**
     * 取关时间
     */
    @TableField(value = "unsubscribe_time")
    @ApiModelProperty(value = "取关时间")
    private Date unsubscribeTime;

    /**
     * 关注时间
     */
    @TableField(value = "subscribe_time")
    @ApiModelProperty(value = "关注时间")
    private Date subscribeTime;

    /**
     * 关注渠道
     */
    @TableField(value = "subscribe_scene")
    @ApiModelProperty(value = "关注渠道")
    private SubscribeSceneEnum subscribeScene;

    /**
     * 是否关注
     */
    @TableField(value = "subscribe_state")
    @ApiModelProperty(value = "是否关注")
    private WhetherEnum subscribeState;

    /**
     * 会员类型
     */
    @TableField(value = "source")
    @ApiModelProperty(value = "会员类型")
    private SourceEnum source;

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
     * isv的appId
     */
    @TableField(value = "app_key", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "isv的appId", hidden = true)
    @JSONField(serialize = false)
    private String appKey;

    private static final long serialVersionUID = 1L;
}