package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.param.shop.ShopInfoGetApiParam;
import com.aquilaflycloud.mdc.param.shop.ShopInfoPageApiParam;
import com.aquilaflycloud.mdc.result.shop.ShopInfoGetResult;
import com.aquilaflycloud.mdc.result.shop.ShopInfoResult;
import com.aquilaflycloud.mdc.service.ShopInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ShopInfoApi
 *
 * @author zengqingjie
 * @date 2020-04-15
 */
@RestController
@Api(tags = "商铺信息查询")
public class ShopInfoApi {

    @Resource
    private ShopInfoService shopInfoService;

    @ApiOperation(value = "获取商铺信息列表", notes = "获取商铺信息列表")
    @ApiMapping(value = "comvita.shop.info.page", method = RequestMethod.POST)
    public IPage<ShopInfoResult> page(ShopInfoPageApiParam param) {
        return shopInfoService.pageShopInfo(param);
    }

    @ApiOperation(value = "根据id获取商铺信息", notes = "根据id获取商铺信息")
    @ApiMapping(value = "comvita.shop.info.get", method = RequestMethod.POST)
    public ShopInfoGetResult getShopInfo(ShopInfoGetApiParam param) {
        return shopInfoService.getShopInfos(param);
    }

}
