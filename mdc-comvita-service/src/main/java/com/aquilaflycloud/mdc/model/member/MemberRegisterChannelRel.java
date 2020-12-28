package com.aquilaflycloud.mdc.model.member;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "member_register_channel_rel")
public class MemberRegisterChannelRel implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 会员id
     */
    @TableField(value = "member_id")
    @ApiModelProperty(value = "会员id")
    private Long memberId;

    /**
     * 渠道id
     */
    @TableField(value = "channel_id")
    @ApiModelProperty(value = "渠道id")
    private Long channelId;

    /**
     * 微信appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value = "微信appId")
    private String appId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "last_update_time", fill = FieldFill.INSERT)
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

    /**
     * isv的appId
     */
    @TableField(value = "app_key", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "isv的appId")
    private String appKey;

    private static final long serialVersionUID = 1L;
}