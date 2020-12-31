package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.param.pre.*;
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







}
