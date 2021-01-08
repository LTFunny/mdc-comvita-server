package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import com.aquilaflycloud.mdc.param.pre.ChangeGoodsInfoParam;
import com.aquilaflycloud.mdc.param.pre.GoodsInfoParam;
import com.aquilaflycloud.mdc.param.pre.PreGoodsInfoListParam;
import com.aquilaflycloud.mdc.param.pre.ReturnGoodsInfoParam;
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
}
