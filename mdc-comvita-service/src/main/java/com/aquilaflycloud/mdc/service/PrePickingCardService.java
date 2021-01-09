package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import com.aquilaflycloud.mdc.model.pre.PrePickingCard;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.PrePickingCardAnalysisResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * PrePickingCardController
 *
 * @author zengqingjie
 * @date 2020-12-28
 */
public interface PrePickingCardService {

    IPage<PrePickingCard> page(PrePickingCardPageParam param);

    PrePickingCardAnalysisResult analysis();

    void batchAdd(PrePickingCardBatchAddParam param);

    void update(PrePickingCardUpdateParam param);

    void validationPickingCard(PrePickingCardValidationParam param);

    PreGoodsInfo validationCardPassWord(PreReservationOrderGoodsParam param);
}
