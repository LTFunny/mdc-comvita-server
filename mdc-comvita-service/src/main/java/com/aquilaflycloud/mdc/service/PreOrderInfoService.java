package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.PreOrderGoodsGetResult;
import com.aquilaflycloud.mdc.result.pre.PreOrderInfoPageResult;
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


    void updateStatConfirmOrder(PreStayConfirmOrderParam param);

    /**
     * 验证订单确认
     * @param param
     * @return
     */
    void validationConfirmOrder(PreConfirmOrderParam param);


    /**
     * 核销提货卡
     * @param param
     */
    void verificationOrder(PreOrderVerificationParam param);


    /**
     * 我的订单列表
     * @param param
     * @return
     */
    IPage<PreOrderInfoPageResult> orderInfoPage(PreOrderInfoPageApiParam param);

    /**
     * 退货列表
     * @param param
     * @return
     */
    IPage<PreOrderInfoPageResult> refundOrderPage(PreOrderInfoPageParam param);


    /**
     * 导购员列表
     * @param param
     * @return
     */
    IPage<PreOrderInfoPageResult> guideMyOrderPage(PreOrderInfoPageParam param);

    /**
     * 我的订单详情
     * @param param
     * @return
     */
    PreOrderInfoPageResult orderInfoGet(PreOrderInfoGetParam param);


    /**
     * 商品确认签收
     * @param param
     * @return
     */
    void confirmReceiptOrderGoods(PreOrderGoodsGetParam param);

    /**
     * 订单确认签收
     * @param param
     * @return
     */
    void confirmReceiptOrder(PreOrderGetParam param);

    void autoConfirmReceiptOrder(PreOrderGoods orderGoods, String operatorName);


    /**
     * 核销提货卡详情
     * @param param
     * @return
     */
    PreOrderGoodsGetResult orderCardGetInfo(PreOrderCardGetParam param);


    void refundOrder(PreOrderRefundParam param);

}
