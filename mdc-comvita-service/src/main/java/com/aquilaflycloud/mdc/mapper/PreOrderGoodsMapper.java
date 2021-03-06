package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.enums.pre.PickingCardStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;

public interface PreOrderGoodsMapper extends AfcBaseMapper<PreOrderGoods> {

    int pickingCardGet(Long orderId, PickingCardStateEnum cardState);

}
