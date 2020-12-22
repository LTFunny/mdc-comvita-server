package com.aquilaflycloud.mdc.param.catalog;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.catalog.CatalogHierarchyTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @className CatalogHierarchyInfoListParam
 *
 * @author zengqingjie
 * @date 2020-08-14
 */
@Data
public class CatalogHierarchyInfoListParam extends AuthParam {
    @ApiModelProperty(value = "类型(catalog.CatalogHierarchyTypeEnum)", required = true)
    @NotNull(message = "类型不能为空")
    private CatalogHierarchyTypeEnum type;

    @ApiModelProperty(value = "过滤字段(true为过滤掉禁用的分类信息)")
    private boolean filterSign;
}
