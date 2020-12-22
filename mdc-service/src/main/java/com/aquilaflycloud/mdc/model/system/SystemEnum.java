package com.aquilaflycloud.mdc.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "system_enum")
public class SystemEnum implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 类型
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "类型")
    private Integer type;

    /**
     * 名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 枚举名称(英文字母)
     */
    @TableField(value = "enum_name")
    @ApiModelProperty(value = "枚举名称(英文字母)")
    private String enumName;

    /**
     * 枚举包名
     */
    @TableField(value = "enum_package")
    @ApiModelProperty(value = "枚举包名")
    private String enumPackage;

    private static final long serialVersionUID = 1L;
}