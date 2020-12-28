package com.aquilaflycloud.mdc.param.catalog;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * CatalogGetParam
 *
 * @author star
 * @date 2020-03-08
 */
@Data
public class CatalogGetParam implements Serializable {
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "分类id不能为空")
    private Long id;
}
