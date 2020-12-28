package com.aquilaflycloud.mdc.service.impl;

import com.aquilaflycloud.dataAuth.bean.DataAuth;
import com.aquilaflycloud.dataAuth.service.DataAuthScopeService;
import com.aquilaflycloud.mdc.feign.consumer.org.DataAuthScopeConsumer;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * DataAuthScopeServiceImpl
 *
 * @author star
 * @date 2019-10-12
 */
@Service
public class DataAuthScopeServiceImpl implements DataAuthScopeService {

    @Resource
    private DataAuthScopeConsumer dataAuthScopeConsumer;

    @Override
    public boolean hasDataAuth(String[] functionNames) {
        String key = "dataAuth" + MdcUtil.getCurrentTenantId();
        for (String functionName : functionNames) {
            Boolean hasDataAuth = RedisUtil.generalRedis().<String, Boolean>opsForHash().get(key, functionName);
            if (hasDataAuth != null) {
                return hasDataAuth;
            }
        }
        return dataAuthScopeConsumer.hasDataAuth(functionNames);
    }

    @Override
    public List<DataAuth> getDataAuthList(Long userId, String[] functionNames) {
        return dataAuthScopeConsumer.getDataAuthList(userId, functionNames);
    }
}
