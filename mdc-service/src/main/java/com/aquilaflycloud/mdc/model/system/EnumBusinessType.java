package com.aquilaflycloud.mdc.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "enum_business_type")
public class EnumBusinessType implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 业务枚举类型
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "业务枚举类型")
    private Integer type;

    /**
     * 业务枚举名
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "业务枚举名")
    private String name;

    /**
     * 枚举包名
     */
    @TableField(value = "enum_package")
    @ApiModelProperty(value = "枚举包名")
    private String enumPackage;

    private static final long serialVersionUID = 1L;
}