package com.aquilaflycloud.mdc.service.impl;

import com.aquilaflycloud.auth.bean.AdminUserDetails;
import com.aquilaflycloud.auth.bean.OperationLog;
import com.aquilaflycloud.auth.bean.Permission;
import com.aquilaflycloud.auth.enums.UserTypeEnum;
import com.aquilaflycloud.auth.service.AuthService;
import com.aquilaflycloud.mdc.feign.consumer.org.AuthConsumer;
import com.aquilaflycloud.mdc.feign.consumer.org.OrganizationConsumer;
import com.aquilaflycloud.mdc.mapper.IsvAppInfoMapper;
import com.aquilaflycloud.mdc.model.IsvAppInfo;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.bean.ServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * IsvAppInfoServiceImpl
 *
 * @author star
 * @date 2019-09-20
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Resource
    private IsvAppInfoMapper isvAppInfoMapper;
    @Resource
    private OrganizationConsumer organizationConsumer;
    @Resource
    private AuthConsumer authConsumer;

    @Override
    public AdminUserDetails getAdminUserDetails(String username) {
        return authConsumer.getAdminUserDetails(username);
    }

    @Override
    public List<Permission> getHasValuePermission() {
        return authConsumer.getHasValuePermission();
    }

    @Override
    public void addOperationLog(OperationLog operationLog) {
        authConsumer.addOperationLog(operationLog);
    }

    @Override
    public IsvAppInfo getIsvByAppId(String appId) {
        IsvAppInfo isv = RedisUtil.valueGet("getIsv" + appId, 7, () -> {
            IsvAppInfo isvAppInfo = isvAppInfoMapper.selectOne(Wrappers.<IsvAppInfo>lambdaQuery().eq(IsvAppInfo::getAppKey, appId));
            if (isvAppInfo != null && isvAppInfo.getType() == UserTypeEnum.TENANTADMIN) {
                List<Long> tenantIdList = null;
                try {
                    tenantIdList = organizationConsumer.getMultiTenantIdList(isvAppInfo.getTenantId());
                } catch (Exception e) {
                    log.error("获取多租户信息失败", e);
                }
                isvAppInfo.setTenantIdList(tenantIdList);
            }
            return isvAppInfo;
        });
        //把isv信息放进上下文中
        ServiceContext.getCurrentContext().set("ISV", isv);
        return isv;
    }
}
