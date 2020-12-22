package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.param.catalog.*;
import com.aquilaflycloud.mdc.result.catalog.CatalogHierarchyNodeInfo;
import com.aquilaflycloud.mdc.result.catalog.CatalogHierarchyPageInfo;
import com.aquilaflycloud.mdc.service.CatalogHierarchyInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * CatalogHierarchyInfoController
 *
 * @author zengqingjie
 * @date 2020-08-13
 */
@RestController
@Api(tags = "分类层级管理")
public class CatalogHierarchyInfoController {
    @Resource
    private CatalogHierarchyInfoService catalogHierarchyInfoService;

    @ApiOperation(value = "分页分类层级信息", notes = "分页分类层级信息")
    @PreAuthorize("hasAuthority('mdc:catalogHierarchy:page')")
    @ApiMapping(value = "backend.mdc.catalog.hierarchy.page", method = RequestMethod.POST, permission = true)
    public IPage<CatalogHierarchyPageInfo> page(CatalogHierarchyInfoPageParam param) {
        return catalogHierarchyInfoService.page(param);
    }

    @ApiOperation(value = "添加分类层级信息", notes = "添加分类层级信息")
    @PreAuthorize("hasAuthority('mdc:catalogHierarchy:add')")
    @ApiMapping(value = "backend.mdc.catalog.hierarchy.add", method = RequestMethod.POST, permission = true)
    public void addCatalogHierarchyInfo(CatalogHierarchyInfoAddParam param) {
        catalogHierarchyInfoService.addCatalogHierarchyInfo(param);
    }

    @ApiOperation(value = "编辑分类层级信息", notes = "编辑分类层级信息")
    @PreAuthorize("hasAuthority('mdc:catalogHierarchy:edit')")
    @ApiMapping(value = "backend.mdc.catalog.hierarchy.edit", method = RequestMethod.POST, permission = true)
    public void editCatalogHierarchyInfo(CatalogHierarchyInfoEditParam param) {
        catalogHierarchyInfoService.editCatalogHierarchyInfo(param);
    }

    @ApiOperation(value = "删除分类层级信息", notes = "删除分类层级信息")
    @PreAuthorize("hasAuthority('mdc:catalogHierarchy:delete')")
    @ApiMapping(value = "backend.mdc.catalog.hierarchy.delete", method = RequestMethod.POST, permission = true)
    public void deleteCatalogHierarchyInfo(CatalogHierarchyInfoDeleteParam param) {
        catalogHierarchyInfoService.deleteCatalogHierarchyInfo(param);
    }

    @ApiOperation(value = "获取分类层级信息列表", notes = "获取分类层级信息列表")
    @PreAuthorize("hasAuthority('mdc:catalogHierarchy:list')")
    @ApiMapping(value = "backend.mdc.catalog.hierarchy.list", method = RequestMethod.POST, permission = true)
    public List<CatalogHierarchyNodeInfo> listCatalogHierarchyInfo(CatalogHierarchyInfoListParam param) {
        return catalogHierarchyInfoService.listCatalogHierarchyInfo(param);
    }
}
