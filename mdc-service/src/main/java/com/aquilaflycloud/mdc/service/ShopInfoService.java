package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.shop.ShopInfo;
import com.aquilaflycloud.mdc.param.shop.*;
import com.aquilaflycloud.mdc.result.shop.ShopInfoDetailResult;
import com.aquilaflycloud.mdc.result.shop.ShopInfoGetResult;
import com.aquilaflycloud.mdc.result.shop.ShopInfoResult;
import com.aquilaflycloud.mdc.result.shop.ShopOperateInfoResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * SystemShopInfoService
 *
 * @author zengqingjie
 * @date 2020-04-07
 */
public interface ShopInfoService {

    IPage<ShopInfo> page(ShopInfoListParam param);

    ShopInfoDetailResult getShopInfo(ShopInfoGetParam param);

    void editShopInfo(ShopInfoEditParam param);

    ShopOperateInfoResult getShopOperateInfo(ShopOperateInfoGetParam param);

    void synchronization(ShopSynchronizationInfoGetParam param);

    BaseResult<String> downloadShopCode(ShopDownloadMiniCodeParam param);

    IPage<ShopInfoResult> pageShopInfo(ShopInfoPageApiParam param);

    ShopInfoGetResult getShopInfos(ShopInfoGetApiParam param);

    BaseResult<String> downloadCode(ShopDownloadCodeParam param);
}

