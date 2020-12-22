package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.shop.ShopCategoryInfo;
import com.aquilaflycloud.mdc.param.shop.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * SystemShopCategoryInfoService
 *
 * @author zengqingjie
 * @date 2020-04-07
 */
public interface ShopCategoryInfoService {

    IPage<ShopCategoryInfo> page(ShopCategoryInfoListParam param);

    ShopCategoryInfo addShopCategoryInfo(ShopCategoryInfoAddParam param);

    ShopCategoryInfo getShopCategoryInfo(ShopCategoryInfoGetParam param);

    void editShopCategoryInfo(ShopCategoryInfoEditParam param);

    void deleteShopCategoryInfo(ShopCategoryInfoGetParam param);

    List<ShopCategoryInfo> listShopCategoryInfo(ShopCategoryInfoListApiParam param);
}

