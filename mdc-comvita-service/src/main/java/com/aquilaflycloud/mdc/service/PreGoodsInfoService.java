package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.GoodsSalesVolumeResult;
import com.aquilaflycloud.mdc.result.pre.PreGoodsInfoResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * Created by zouliyong
 */
public interface PreGoodsInfoService {
    IPage<PreGoodsInfo> pagePreGoodsInfoList(PreGoodsInfoListParam param);

    void addPreGoodsInfo(GoodsInfoAddParam param);

    void editPreGoodsInfo(GoodsInfoEditParam param);

    void changeGoodsState(ChangeGoodsStateParam param);

    PreGoodsInfoResult getGoodsInfo(GoodsInfoGetParam param);

    GoodsSalesVolumeResult goodsVolume(GoodsSaleNumParam param);

}
