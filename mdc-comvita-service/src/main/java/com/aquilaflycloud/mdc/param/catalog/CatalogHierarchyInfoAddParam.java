package com.aquilaflycloud.mdc.param.catalog;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.catalog.CatalogHierarchyTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @className CatalogHierarchyInfoAddParam
 *
 * @author zengqingjie
 * @date 2020-08-13
 */
@Data
public class CatalogHierarchyInfoAddParam extends AuthParam {

    @ApiModelProperty(value = "父业态id", required = true)
    @NotNull(message = "父业态id不能为空")
    private Long pId;

    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "图标")
    private String picUrl;

    @ApiModelProperty(value = "类型(catalog.CatalogHierarchyTypeEnum)", required = true)
    @NotNull(message = "类型不能为空")
    private CatalogHierarchyTypeEnum type;

    @ApiModelProperty(value = "排序", required = true)
    @NotNull(message = "排序不能为空")
    private Integer sort;
}
