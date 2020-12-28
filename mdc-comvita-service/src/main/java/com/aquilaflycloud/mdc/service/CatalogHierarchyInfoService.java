package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.catalog.CatalogHierarchyInfo;
import com.aquilaflycloud.mdc.param.catalog.*;
import com.aquilaflycloud.mdc.result.catalog.CatalogHierarchyNodeInfo;
import com.aquilaflycloud.mdc.result.catalog.CatalogHierarchyPageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * CatalogHierarchyInfoService
 *
 * @author zengqingjie
 * @date 2020-08-13
 */
public interface CatalogHierarchyInfoService {

    void addCatalogHierarchyInfo(CatalogHierarchyInfoAddParam param);

    void editCatalogHierarchyInfo(CatalogHierarchyInfoEditParam param);

    void deleteCatalogHierarchyInfo(CatalogHierarchyInfoDeleteParam param);

    List<CatalogHierarchyNodeInfo> listCatalogHierarchyInfo(CatalogHierarchyInfoListParam param);

    List<Long> getCurrAndChildrenIds(List<Long> ids);

    IPage<CatalogHierarchyPageInfo> page(CatalogHierarchyInfoPageParam param);
}
