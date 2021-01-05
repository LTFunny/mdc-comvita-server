package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.PreOrderGoodsGetResult;
import com.aquilaflycloud.mdc.result.pre.PreOrderInfoPageResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    int addStatConfirmOrder(PreStayConfirmOrderParam param);


    /**
     * 验证订单确认
     * @param param
     * @return
     */
    @Transactional
    void validationConfirmOrder(PreConfirmOrderParam param);


    /**
     * 核销提货卡
     * @param param
     */
    @Transactional
    void verificationOrder(PreOrderVerificationParam param);


    /**
     * 我的订单列表
     * @param param
     * @return
     */
    IPage<PreOrderInfoPageResult> orderInfoPage(PreOrderInfoPageParam param);

    /**
     * 我的订单详情
     * @param param
     * @return
     */
    PreOrderInfoPageResult orderInfoGet(PreOrderInfoGetParam param);


    /**
     * 确认签收
     * @param param
     * @return
     */
    void confirmReceiptOrder(PreOrderInfoGetParam param);


    /**
     * 核销提货卡详情
     * @param param
     * @return
     */
    PreOrderGoodsGetResult orderCardGetInfo(PreOrderCardGetParam param);
}
