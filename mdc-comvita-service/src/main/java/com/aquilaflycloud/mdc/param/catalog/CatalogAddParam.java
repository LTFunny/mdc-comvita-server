package com.aquilaflycloud.mdc.param.catalog;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * CatalogAddParam
 *
 * @author star
 * @date 2020-03-08
 */
@Data
public class CatalogAddParam implements Serializable {
    @ApiModelProperty(value = "分类名称", required = true)
    @NotBlank(message = "分类名称不能为空")
    private String catalogName;

    @ApiModelProperty(value = "分类排序(按数字倒序排列)", required = true)
    @NotNull(message = "分类排序不能为空")
    private Integer catalogOrder;
}
