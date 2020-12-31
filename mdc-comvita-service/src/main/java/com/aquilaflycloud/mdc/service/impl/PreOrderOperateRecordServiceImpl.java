package com.aquilaflycloud.mdc.service.impl;

import com.aquilaflycloud.mdc.mapper.PreOrderOperateRecordMapper;
import com.aquilaflycloud.mdc.model.pre.PreOrderOperateRecord;
import com.aquilaflycloud.mdc.service.PreOrderOperateRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author pengyongliang
 * @Date 2020/12/31 9:42
 * @Version 1.0
 */
@Service
public class PreOrderOperateRecordServiceImpl implements PreOrderOperateRecordService {

    @Resource
    private PreOrderOperateRecordMapper orderOperateRecordMapper;

    @Override
    public void addOrderOperateRecordLog(Long tenantId, String operatorName, Long orderId, String content) {
        //记录操作日志
        PreOrderOperateRecord preOrderOperateRecord = new PreOrderOperateRecord();
        preOrderOperateRecord.setTenantId(tenantId);
        preOrderOperateRecord.setOperatorName(operatorName);
        preOrderOperateRecord.setOrderId(orderId);
        preOrderOperateRecord.setOperatorContent(content);
        orderOperateRecordMapper.insert(preOrderOperateRecord);
    }
}
