package com.aquilaflycloud.mdc.model;

import com.aquilaflycloud.auth.bean.Isv;
import com.aquilaflycloud.auth.enums.UserTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "isv_app_info")
public class IsvAppInfo extends Isv implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * isv应用id
     */
    @TableField(value = "app_key")
    @ApiModelProperty(value = "isv应用id")
    private String appKey;

    /**
     * 第三方appId
     */
    @TableField(value = "other_app_id")
    @ApiModelProperty(value = "第三方appId")
    private String otherAppId;

    /**
     * 类型
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "类型")
    private UserTypeEnum type;

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