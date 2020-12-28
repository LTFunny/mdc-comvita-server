package com.aquilaflycloud.mdc.model.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.wechat.UserSourceEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "wechat_user_analysis")
public class WechatUserAnalysis implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 微信公众号appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value="微信公众号appId")
    private String appId;

    /**
     * 数据的日期
     */
    @TableField(value = "ref_date")
    @ApiModelProperty(value="数据的日期")
    private Date refDate;

    /**
     * 用户的渠道
     */
    @TableField(value = "user_source")
    @ApiModelProperty(value="用户的渠道")
    private UserSourceEnum userSource;

    /**
     * 新增的用户数量
     */
    @TableField(value = "new_user")
    @ApiModelProperty(value="新增的用户数量")
    private Integer newUser;

    /**
     * 新增的用户数量
     */
    @TableField(value = "cancel_user")
    @ApiModelProperty(value="新增的用户数量")
    private Integer cancelUser;

    /**
     * 净增的用户数量
     */
    @TableField(value = "increase_user")
    @ApiModelProperty(value="净增的用户数量")
    private Integer increaseUser;

    /**
     * 新增的用户数量
     */
    @TableField(value = "cumulate_user")
    @ApiModelProperty(value="新增的用户数量")
    private Integer cumulateUser;

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