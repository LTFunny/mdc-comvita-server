package com.aquilaflycloud.mdc.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "system_config")
public class SystemConfig implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 配置码
     */
    @TableField(value = "config_code")
    @ApiModelProperty(value = "配置码")
    private String configCode;

    /**
     * 配置值
     */
    @TableField(value = "config_value")
    @ApiModelProperty(value = "配置值")
    private String configValue;

    /**
     * 配置说明
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "配置说明")
    private String remark;

    private static final long serialVersionUID = 1L;
}