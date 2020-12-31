package com.aquilaflycloud.mdc.service;

/**
 * @Author pengyongliang
 * @Date 2020/12/31 9:41
 * @Version 1.0
 */
public interface PreOrderOperateRecordService {


    void addOrderOperateRecordLog(Long tenantId,String operatorName,Long orderId,String content);
}
