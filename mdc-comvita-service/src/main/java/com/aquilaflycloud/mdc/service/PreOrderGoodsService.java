package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.param.pre.PreOrderGoodsPageParam;
import com.aquilaflycloud.mdc.param.pre.PreReservationOrderGoodsParam;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author pengyongliang
 * @Date 2020/12/31 11:37
 * @Version 1.0
 */
public interface PreOrderGoodsService {

    /**
     * 预约自提
     * @param param
     */
    void reservationOrderGoods(PreReservationOrderGoodsParam param);


    /**
     * 预约自提列表
     * @param param
     * @return
     */
    IPage<PreOrderGoods> pagePreOrderGoods(PreOrderGoodsPageParam param);


    /**
     * 预约自提详情
     * @param param
     * @return
     */
    PreOrderGoods getPreOrderGoods(PreReservationOrderGoodsParam param);


}
