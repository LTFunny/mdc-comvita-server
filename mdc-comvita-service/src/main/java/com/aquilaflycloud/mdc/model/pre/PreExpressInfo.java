package com.aquilaflycloud.mdc.model.pre;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 快递鸟快递编码信息表
 */
@Data
@TableName(value = "pre_express_info")
public class PreExpressInfo implements Serializable {
    /**
     * 快递编码
     */
    @TableField(value = "express_code")
    @ApiModelProperty(value = "快递编码")
    private String expressCode;

    /**
     * 快递名称
     */
    @TableField(value = "express_name")
    @ApiModelProperty(value = "快递名称")
    private String expressName;

    /**
     * 快递名称
     */
    @TableField(value = "sort")
    @ApiModelProperty(value = "排序")
    private Integer sort;

    private static final long serialVersionUID = 1L;
}
