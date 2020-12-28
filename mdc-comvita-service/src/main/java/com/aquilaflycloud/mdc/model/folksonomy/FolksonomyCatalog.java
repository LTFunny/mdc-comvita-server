package com.aquilaflycloud.mdc.model.folksonomy;

import com.aquilaflycloud.mdc.enums.folksonomy.FolksonomyTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "folksonomy_catalog")
public class FolksonomyCatalog implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 父id
     */
    @TableField(value = "pid")
    @ApiModelProperty(value = "父id")
    private Long pid;

    /**
     * 目录名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "目录名称")
    private String name;

    /**
     * 目录标签类型
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "目录标签类型")
    private FolksonomyTypeEnum type;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    private static final long serialVersionUID = 1L;
}