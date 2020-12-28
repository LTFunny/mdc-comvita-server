package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.catalog.CatalogInfo;
import com.aquilaflycloud.mdc.param.catalog.CatalogAddParam;
import com.aquilaflycloud.mdc.param.catalog.CatalogEditParam;
import com.aquilaflycloud.mdc.param.catalog.CatalogGetParam;
import com.aquilaflycloud.mdc.param.catalog.CatalogPageParam;
import com.aquilaflycloud.mdc.result.catalog.CatalogResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface CatalogService {

    IPage<CatalogResult> pageCouponCatalog(CatalogPageParam param);

    List<CatalogInfo> listCouponCatalogInfo();

    void addCouponCatalog(CatalogAddParam param);

    void editCouponCatalog(CatalogEditParam param);

    void deleteCouponCatalog(CatalogGetParam param);

    IPage<CatalogResult> pageExchangeCatalog(CatalogPageParam param);

    List<CatalogInfo> listExchangeCatalogInfo();

    void addExchangeCatalog(CatalogAddParam param);

    void editExchangeCatalog(CatalogEditParam param);

    void deleteExchangeCatalog(CatalogGetParam param);

    void toggleCatalog(CatalogGetParam param);

    CatalogInfo getCatalog(CatalogGetParam param);

    List<CatalogInfo> listCouponCatalog();

    List<CatalogInfo> listExchangeCatalog();
}
