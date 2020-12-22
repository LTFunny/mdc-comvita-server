package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.shop.ShopComplaintInfo;
import com.aquilaflycloud.mdc.param.shop.*;
import com.aquilaflycloud.mdc.result.shop.ShopComplaintStatisticsResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface ShopComplaintInfoService {

    IPage<ShopComplaintInfo> page(ShopComplaintInfoPageParam param);

    ShopComplaintInfo get(ShopComplaintInfoGetParam param);

    void edit(ShopComplaintInfoEditParam param);

    ShopComplaintStatisticsResult statistics(ShopComplaintStatisticsParam param);

    IPage<ShopComplaintInfo> getPageComplaintInfo(ShopComplaintInfoPageApiParam param);

    void addComplaintInfo(ShopComplaintInfoAddApiParam param);

    void delete(ShopComplaintInfoDeleteParam param);
}
