package com.aquilaflycloud.mdc.service;

/**
 * <p>
 * 产品信息服务类(不包含租户解析)
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-18
 */
public interface TicketProductInfoNormalService {
    /**
     * 获取第三方接口产品数据列表，并保存
     */
    void getInterfaceProductInfo();
}
