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
 * CouponCatalogController
 *
 * @author star
 * @date 2020-03-08
 */
@RestController
@Api(tags = "优惠券分类管理")
public class CouponCatalogController {

    @Resource
    private CatalogService catalogService;

    @ApiOperation(value = "优惠券分类列表查询(分页)", notes = "优惠券分类列表查询(分页)")
    @PreAuthorize("hasAuthority('mdc:couponCatalog:list')")
    @ApiMapping(value = "backend.mdc.coupon.catalog.page", method = RequestMethod.POST, permission = true)
    public IPage<CatalogResult> pageCatalog(CatalogPageParam param) {
        return catalogService.pageCouponCatalog(param);
    }

    @ApiOperation(value = "优惠券分类列表", notes = "优惠券分类列表")
    @PreAuthorize("hasAuthority('mdc:couponCatalog:list')")
    @ApiMapping(value = "backend.mdc.coupon.catalog.list", method = RequestMethod.POST, permission = true)
    public List<CatalogInfo> listCatalog() {
        return catalogService.listCouponCatalogInfo();
    }

    @ApiOperation(value = "新增优惠券分类", notes = "新增优惠券分类")
    @PreAuthorize("hasAuthority('mdc:couponCatalog:add')")
    @ApiMapping(value = "backend.mdc.coupon.catalog.add", method = RequestMethod.POST, permission = true)
    public void addCatalog(CatalogAddParam param) {
        catalogService.addCouponCatalog(param);
    }

    @ApiOperation(value = "编辑优惠券分类", notes = "编辑优惠券分类")
    @PreAuthorize("hasAuthority('mdc:couponCatalog:edit')")
    @ApiMapping(value = "backend.mdc.coupon.catalog.edit", method = RequestMethod.POST, permission = true)
    public void editCatalog(CatalogEditParam param) {
        catalogService.editCouponCatalog(param);
    }

    @ApiOperation(value = "停用/启用优惠券分类", notes = "停用/启用优惠券分类")
    @PreAuthorize("hasAuthority('mdc:couponCatalog:edit')")
    @ApiMapping(value = "backend.mdc.coupon.catalog.toggle", method = RequestMethod.POST, permission = true)
    public void toggleCatalog(CatalogGetParam param) {
        catalogService.toggleCatalog(param);
    }

    @ApiOperation(value = "获取优惠券分类", notes = "获取优惠券分类")
    @PreAuthorize("hasAuthority('mdc:couponCatalog:get')")
    @ApiMapping(value = "backend.mdc.coupon.catalog.get", method = RequestMethod.POST, permission = true)
    public CatalogInfo getCatalog(CatalogGetParam param) {
        return catalogService.getCatalog(param);
    }

    @ApiOperation(value = "删除优惠券分类", notes = "删除优惠券分类")
    @PreAuthorize("hasAuthority('mdc:couponCatalog:delete')")
    @ApiMapping(value = "backend.mdc.coupon.catalog.delete", method = RequestMethod.POST, permission = true)
    public void deleteCatalog(CatalogGetParam param) {
        catalogService.deleteCouponCatalog(param);
    }


}
