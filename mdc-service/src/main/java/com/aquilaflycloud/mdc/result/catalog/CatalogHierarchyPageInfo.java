package com.aquilaflycloud.mdc.result.catalog;

import com.aquilaflycloud.mdc.model.catalog.CatalogHierarchyInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className CatalogHierarchyPageInfo
 *
 * @author zengqingjie
 * @date 2020-08-13
 */
@Data
public class CatalogHierarchyPageInfo extends CatalogHierarchyInfo {

    @ApiModelProperty(value = "父级分类名称")
    private String parentName;
}
