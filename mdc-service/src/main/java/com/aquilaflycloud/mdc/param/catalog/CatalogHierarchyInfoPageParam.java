package com.aquilaflycloud.mdc.param.catalog;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.catalog.CatalogHierarchyTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className CatalogHierarchyInfoPageParam
 *
 * @author zengqingjie
 * @date 2020-08-13
 */
@Data
public class CatalogHierarchyInfoPageParam extends PageAuthParam {
    @ApiModelProperty(value = "类型(catalog.CatalogHierarchyTypeEnum)")
    private CatalogHierarchyTypeEnum type;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "分类id")
    private Long id;
}
