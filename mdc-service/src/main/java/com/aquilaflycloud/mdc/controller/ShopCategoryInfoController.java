package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.shop.ShopCategoryInfo;
import com.aquilaflycloud.mdc.param.shop.ShopCategoryInfoAddParam;
import com.aquilaflycloud.mdc.param.shop.ShopCategoryInfoEditParam;
import com.aquilaflycloud.mdc.param.shop.ShopCategoryInfoGetParam;
import com.aquilaflycloud.mdc.param.shop.ShopCategoryInfoListParam;
import com.aquilaflycloud.mdc.service.ShopCategoryInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * SystemShopCategoryInfoController
 *
 * @author zengqingjie
 * @date 2020-04-07
 */
@RestController
@Api(tags = "系统商户分类信息查询")
public class ShopCategoryInfoController {

    @Resource
    private ShopCategoryInfoService shopCategoryInfoService;

    @ApiOperation(value = "查询系统商户分类信息(分页)", notes = "查询系统商户分类信息(分页)")
    @PreAuthorize("hasAuthority('mdc:shop:category:info:page')")
    @ApiMapping(value = "backend.mdc.shop.category.info.page", method = RequestMethod.POST, permission = true)
    public IPage<ShopCategoryInfo> page(ShopCategoryInfoListParam param) {
        return shopCategoryInfoService.page(param);
    }

    @ApiOperation("获取系统商户分类信息详情")
    @PreAuthorize("hasAuthority('mdc:shop:category:info:get')")
    @ApiMapping(value = "backend.mdc.shop.category.info.get", method = RequestMethod.POST, permission = true)
    public ShopCategoryInfo getShopCategoryInfo(ShopCategoryInfoGetParam param) {
        return shopCategoryInfoService.getShopCategoryInfo(param);
    }

    @ApiOperation("新增系统商户分类信息详情")
    @PreAuthorize("hasAuthority('mdc:shop:category:info:add')")
    @ApiMapping(value = "backend.mdc.shop.category.info.add", method = RequestMethod.POST, permission = true)
    public ShopCategoryInfo addShopCategoryInfo(ShopCategoryInfoAddParam param) {
        return shopCategoryInfoService.addShopCategoryInfo(param);
    }

    @ApiOperation("更新系统商户分类信息")
    @PreAuthorize("hasAuthority('mdc:shop:category:info:edit')")
    @ApiMapping(value = "backend.mdc.shop.category.info.edit", method = RequestMethod.POST, permission = true)
    public void editShopCategoryInfo(ShopCategoryInfoEditParam param) {
        shopCategoryInfoService.editShopCategoryInfo(param);
    }

    @ApiOperation("删除系统商户分类信息")
    @PreAuthorize("hasAuthority('mdc:shop:category:info:delete')")
    @ApiMapping(value = "backend.mdc.shop.category.info.delete", method = RequestMethod.POST, permission = true)
    public void deleteShopCategoryInfo(ShopCategoryInfoGetParam param) {
        shopCategoryInfoService.deleteShopCategoryInfo(param);
    }
}
