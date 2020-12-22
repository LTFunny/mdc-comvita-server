package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.model.shop.ShopCategoryInfo;
import com.aquilaflycloud.mdc.param.shop.ShopCategoryInfoListApiParam;
import com.aquilaflycloud.mdc.service.ShopCategoryInfoService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * ShopCategoryInfoApi
 *
 * @author zengqingjie
 * @date 2020-04-15
 */
@RestController
@Api(tags = "商铺分类信息查询")
public class ShopCategoryInfoApi {

    @Resource
    private ShopCategoryInfoService shopCategoryInfoService;

    @ApiOperation(value = "获取商铺分类信息列表", notes = "获取商铺分类信息列表")
    @ApiMapping(value = "mdc.shop.category.list", method = RequestMethod.POST)
    public List<ShopCategoryInfo> listShopCategoryInfo(ShopCategoryInfoListApiParam param) {
        return shopCategoryInfoService.listShopCategoryInfo(param);
    }

}
