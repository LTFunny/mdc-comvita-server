package com.aquilaflycloud.mdc.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "system_increment")
public class SystemIncrement implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 自增主键key
     */
    @TableField(value = "pk_key")
    @ApiModelProperty(value = "自增主键key")
    private String pkKey;

    /**
     * 自增主键值
     */
    @TableField(value = "pk_value")
    @ApiModelProperty(value = "自增主键值")
    private Long pkValue;

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