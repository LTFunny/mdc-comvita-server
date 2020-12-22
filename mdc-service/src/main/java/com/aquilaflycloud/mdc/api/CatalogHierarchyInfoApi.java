package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.param.catalog.CatalogHierarchyInfoListParam;
import com.aquilaflycloud.mdc.result.catalog.CatalogHierarchyNodeInfo;
import com.aquilaflycloud.mdc.service.CatalogHierarchyInfoService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * CatalogHierarchyInfoApi
 *
 * @author zengqingjie
 * @date 2020-08-14
 */
@RestController
@Api(tags = "分类层级信息")
public class CatalogHierarchyInfoApi {
    @Resource
    private CatalogHierarchyInfoService catalogHierarchyInfoService;

    @ApiOperation(value = "获取分类层级信息列表", notes = "获取分类层级信息列表")
    @ApiMapping(value = "mdc.catalog.hierarchy.list", method = RequestMethod.POST)
    public List<CatalogHierarchyNodeInfo> listCatalogHierarchyInfo(CatalogHierarchyInfoListParam param) {
        param.setFilterSign(true);
        return catalogHierarchyInfoService.listCatalogHierarchyInfo(param);
    }
}
