package com.aquilaflycloud.mdc.param.catalog;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * CatalogEditParam
 *
 * @author star
 * @date 2020-03-08
 */
@Data
public class CatalogEditParam implements Serializable {
    @ApiModelProperty(value="id")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value="分类名称")
    private String catalogName;

    @ApiModelProperty(value="分类排序")
    private Integer catalogOrder;
}
