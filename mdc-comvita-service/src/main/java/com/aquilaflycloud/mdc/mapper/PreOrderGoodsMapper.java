package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.enums.pre.PickingCardStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.param.pre.PreOrderGoodsPageParam;
import com.aquilaflycloud.mdc.result.pre.PreOrderGoodsPageResult;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

public interface PreOrderGoodsMapper extends AfcBaseMapper<PreOrderGoods> {

    int pickingCardGet(Long orderId, PickingCardStateEnum cardState);

}