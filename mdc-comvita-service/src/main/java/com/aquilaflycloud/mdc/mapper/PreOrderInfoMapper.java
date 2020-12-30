package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.enums.pre.PickingCardStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;

import java.util.List;

public interface PreOrderInfoMapper extends AfcBaseMapper<PreOrderInfo> {

    String getMaxOrderCode();

    List<String> pickingCardGet(Long orderId, PickingCardStateEnum cardState);
}