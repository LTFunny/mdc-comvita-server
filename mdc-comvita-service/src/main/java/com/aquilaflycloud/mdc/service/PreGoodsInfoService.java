package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.GoodsSalesVolumeResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * Created by zouliyong
 */
public interface PreGoodsInfoService {
    IPage<PreGoodsInfo> pagePreGoodsInfoList(PreGoodsInfoListParam param);

    void addPreGoodsInfo(ReturnGoodsInfoParam param);

    void editPreGoodsInfo(ReturnGoodsInfoParam param);

    void changeGoodsType(ChangeGoodsInfoParam param);

    ReturnGoodsInfoParam goodsData(GoodsInfoParam param);

    GoodsSalesVolumeResult goodsVolume(GoodsSaleNumParam param);

}
