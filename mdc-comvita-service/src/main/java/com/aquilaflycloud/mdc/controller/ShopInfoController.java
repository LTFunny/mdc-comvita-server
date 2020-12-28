package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.shop.ShopInfo;
import com.aquilaflycloud.mdc.param.shop.*;
import com.aquilaflycloud.mdc.result.shop.ShopInfoDetailResult;
import com.aquilaflycloud.mdc.result.shop.ShopOperateInfoResult;
import com.aquilaflycloud.mdc.service.ShopInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * SystemShopInfoController
 *
 * @author zengqingjie
 * @date 2020-04-07
 */
@RestController
@Api(tags = "系统商户信息查询")
public class ShopInfoController {

    @Resource
    private ShopInfoService shopInfoService;

    @ApiOperation(value = "查询系统商户信息(分页)", notes = "查询系统商户信息(分页)")
    @PreAuthorize("hasAuthority('mdc:shop:info:page')")
    @ApiMapping(value = "backend.comvita.shop.info.page", method = RequestMethod.POST, permission = true)
    public IPage<ShopInfo> page(ShopInfoListParam param) {
        return shopInfoService.page(param);
    }

    @ApiOperation("获取系统商户信息详情")
    @PreAuthorize("hasAuthority('mdc:shop:info:get')")
    @ApiMapping(value = "backend.comvita.shop.info.get", method = RequestMethod.POST, permission = true)
    public ShopInfoDetailResult getShopInfo(ShopInfoGetParam param) {
        return shopInfoService.getShopInfo(param);
    }

    @ApiOperation("更新系统商户信息")
    @PreAuthorize("hasAuthority('mdc:shop:info:edit')")
    @ApiMapping(value = "backend.comvita.shop.info.edit", method = RequestMethod.POST, permission = true)
    public void editShopInfo(ShopInfoEditParam param) {
        shopInfoService.editShopInfo(param);
    }

    @ApiOperation("获取系统商户运营信息")
    @PreAuthorize("hasAuthority('mdc:shop:info:page')")
    @ApiMapping(value = "backend.comvita.shop.info.getOperateInfo", method = RequestMethod.POST, permission = true)
    public ShopOperateInfoResult getShopOperateInfo(ShopOperateInfoGetParam param) {
        return shopInfoService.getShopOperateInfo(param);
    }

    @ApiOperation("同步系统商户信息")
    @PreAuthorize("hasAuthority('mdc:shop:info:synchronization')")
    @ApiMapping(value = "backend.comvita.shop.info.synchronization", method = RequestMethod.POST, permission = true)
    public void synchronization(ShopSynchronizationInfoGetParam param) {
        shopInfoService.synchronization(param);
    }

    @ApiOperation("批量下载商户二维码")
    @PreAuthorize("hasAuthority('mdc:shop:info:page')")
    @ApiMapping(value = "backend.comvita.shop.info.downloadShopCode", method = RequestMethod.POST, permission = true)
    public BaseResult<String> downloadShopCode(ShopDownloadMiniCodeParam param) {
        return shopInfoService.downloadShopCode(param);
    }

    @ApiOperation("下载商户二维码")
    @PreAuthorize("hasAuthority('mdc:shop:info:get')")
    @ApiMapping(value = "backend.comvita.shop.info.download", method = RequestMethod.POST, permission = true)
    public BaseResult<String> downloadCode(ShopDownloadCodeParam param) {
        return shopInfoService.downloadCode(param);
    }
}
