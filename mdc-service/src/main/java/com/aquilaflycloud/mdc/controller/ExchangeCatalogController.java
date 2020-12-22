package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.catalog.CatalogInfo;
import com.aquilaflycloud.mdc.param.catalog.CatalogAddParam;
import com.aquilaflycloud.mdc.param.catalog.CatalogEditParam;
import com.aquilaflycloud.mdc.param.catalog.CatalogGetParam;
import com.aquilaflycloud.mdc.param.catalog.CatalogPageParam;
import com.aquilaflycloud.mdc.result.catalog.CatalogResult;
import com.aquilaflycloud.mdc.service.CatalogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * ExchangeCatalogController
 *
 * @author star
 * @date 2020-03-15
 */
@RestController
@Api(tags = "兑换商品分类管理")
public class ExchangeCatalogController {

    @Resource
    private CatalogService catalogService;

    @ApiOperation(value = "兑换商品分类列表查询(分页)", notes = "兑换商品分类列表查询(分页)")
    @PreAuthorize("hasAuthority('mdc:exchangeCatalog:list')")
    @ApiMapping(value = "backend.mdc.exchange.catalog.page", method = RequestMethod.POST, permission = true)
    public IPage<CatalogResult> pageCatalog(CatalogPageParam param) {
        return catalogService.pageExchangeCatalog(param);
    }

    @ApiOperation(value = "兑换商品分类列表", notes = "兑换商品分类列表")
    @PreAuthorize("hasAuthority('mdc:exchangeCatalog:list')")
    @ApiMapping(value = "backend.mdc.exchange.catalog.list", method = RequestMethod.POST, permission = true)
    public List<CatalogInfo> listCatalog() {
        return catalogService.listExchangeCatalogInfo();
    }

    @ApiOperation(value = "新增兑换商品分类", notes = "新增兑换商品分类")
    @PreAuthorize("hasAuthority('mdc:exchangeCatalog:add')")
    @ApiMapping(value = "backend.mdc.exchange.catalog.add", method = RequestMethod.POST, permission = true)
    public void addCatalog(CatalogAddParam param) {
        catalogService.addExchangeCatalog(param);
    }

    @ApiOperation(value = "编辑兑换商品分类", notes = "编辑兑换商品分类")
    @PreAuthorize("hasAuthority('mdc:exchangeCatalog:edit')")
    @ApiMapping(value = "backend.mdc.exchange.catalog.edit", method = RequestMethod.POST, permission = true)
    public void editCatalog(CatalogEditParam param) {
        catalogService.editExchangeCatalog(param);
    }

    @ApiOperation(value = "停用/启用兑换商品分类", notes = "停用/启用兑换商品分类")
    @PreAuthorize("hasAuthority('mdc:exchangeCatalog:edit')")
    @ApiMapping(value = "backend.mdc.exchange.catalog.toggle", method = RequestMethod.POST, permission = true)
    public void toggleCatalog(CatalogGetParam param) {
        catalogService.toggleCatalog(param);
    }

    @ApiOperation(value = "获取兑换商品分类", notes = "获取兑换商品分类")
    @PreAuthorize("hasAuthority('mdc:exchangeCatalog:get')")
    @ApiMapping(value = "backend.mdc.exchange.catalog.get", method = RequestMethod.POST, permission = true)
    public CatalogInfo getCatalog(CatalogGetParam param) {
        return catalogService.getCatalog(param);
    }

    @ApiOperation(value = "删除兑换商品分类", notes = "删除兑换商品分类")
    @PreAuthorize("hasAuthority('mdc:exchangeCatalog:delete')")
    @ApiMapping(value = "backend.mdc.exchange.catalog.delete", method = RequestMethod.POST, permission = true)
    public void deleteCatalog(CatalogGetParam param) {
        catalogService.deleteExchangeCatalog(param);
    }
}
