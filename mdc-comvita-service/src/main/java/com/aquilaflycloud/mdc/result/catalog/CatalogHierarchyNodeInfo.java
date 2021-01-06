package com.aquilaflycloud.mdc.result.catalog;

import com.aquilaflycloud.mdc.model.catalog.CatalogHierarchyInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @className CatalogHierarchyNodeInfoResult
 *
 * @author zengqingjie
 * @date 2020-08-13
 */
@Data
public class CatalogHierarchyNodeInfo extends CatalogHierarchyInfo {

    @ApiModelProperty(value = "子节点")
    private List<CatalogHierarchyNodeInfo> children;
}
