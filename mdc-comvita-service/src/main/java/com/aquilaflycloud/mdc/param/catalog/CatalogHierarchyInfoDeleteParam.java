package com.aquilaflycloud.mdc.param.catalog;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @className CatalogHierarchyInfoEditParam
 *
 * @author zengqingjie
 * @date 2020-08-13
 */
@Data
public class CatalogHierarchyInfoDeleteParam extends AuthParam {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;
}
