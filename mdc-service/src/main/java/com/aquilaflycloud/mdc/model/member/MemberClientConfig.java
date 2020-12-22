package com.aquilaflycloud.mdc.model.member;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "member_client_config")
public class MemberClientConfig implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 微信或支付宝appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value="微信或支付宝appId")
    private String appId;

    /**
     * 配置详情
     */
    @TableField(value = "config_content")
    @ApiModelProperty(value="配置详情")
    private String configContent;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "last_update_time")
    @ApiModelProperty(value="最后更新时间")
    private Date lastUpdateTime;

    /**
     * 租户id
     */
    @TableField(value = "tenant_id")
    @ApiModelProperty(value="租户id")
    private Long tenantId;

    /**
     * 子租户id
     */
    @TableField(value = "sub_tenant_id")
    @ApiModelProperty(value="子租户id")
    private Long subTenantId;

    /**
     * 创建记录人id
     */
    @TableField(value = "creator_id")
    @ApiModelProperty(value="创建记录人id")
    private Long creatorId;

    /**
     * 创建记录人名称
     */
    @TableField(value = "creator_name")
    @ApiModelProperty(value="创建记录人名称")
    private String creatorName;

    /**
     * 创建用户所属部门ids
     */
    @TableField(value = "creator_org_ids")
    @ApiModelProperty(value="创建用户所属部门ids")
    private String creatorOrgIds;

    /**
     * 创建用户所属部门名称
     */
    @TableField(value = "creator_org_names")
    @ApiModelProperty(value="创建用户所属部门名称")
    private String creatorOrgNames;

    /**
     * 最后操作人id
     */
    @TableField(value = "last_operator_id")
    @ApiModelProperty(value="最后操作人id")
    private Long lastOperatorId;

    /**
     * 最后操作人名称
     */
    @TableField(value = "last_operator_name")
    @ApiModelProperty(value="最后操作人名称")
    private String lastOperatorName;

    /**
     * isv的appId
     */
    @TableField(value = "app_key")
    @ApiModelProperty(value="isv的appId")
    private String appKey;

    private static final long serialVersionUID = 1L;
}