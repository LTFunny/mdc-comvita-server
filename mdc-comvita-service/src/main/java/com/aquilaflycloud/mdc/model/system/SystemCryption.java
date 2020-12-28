package com.aquilaflycloud.mdc.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "system_cryption")
public class SystemCryption implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 内容
     */
    @TableField(value = "content")
    @ApiModelProperty(value = "内容")
    private String content;

    private static final long serialVersionUID = 1L;
}