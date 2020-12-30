package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.PreOrderInfoGetResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author pengyongliang
 * @Date 2020/12/29 16:27
 * @Version 1.0
 */
public interface PreOrderInfoService{

    /**
     * 生成待确认订单
     * @param param
     * @return
     */
    int addStatConfirmOrder(PreStayConfirmOrderParam param);

    /**
     * 订单确认列表
     * @param param
     * @return
     */
    IPage<PreOrderInfo> pagePreOrderInfo(PreOrderInfoPageParam param);


    /**
     * 订单确认详情
     * @param param
     * @return
     */
    PreOrderInfoGetResult getConfirmOrderInfo(PreOrderInfoGetParam param);


    /**
     * 验证订单确认
     * @param param
     * @return
     */
    void validationConfirmOrder(PreConfirmOrderParam param);


    /**
     * 预约自提
     * @param param
     */
    void reservationOrderGoods(PreReservationOrderGoodsParam param);

}
