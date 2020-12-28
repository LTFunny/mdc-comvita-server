package com.aquilaflycloud.mdc.service;

import cn.hutool.json.JSONObject;

/**
 * <p>
 * 回调函数记录服务类
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-19
 */
public interface TicketCallbackFunctionRecordService {
    /**
     * 处理订单回调
     * @param orderNo
     * @param otaOrderNo
     * @param orderInfoStr
     * @return
     */
    JSONObject ticketOrderCallbackHandler(String orderNo, String otaOrderNo, String orderInfoStr);
}
