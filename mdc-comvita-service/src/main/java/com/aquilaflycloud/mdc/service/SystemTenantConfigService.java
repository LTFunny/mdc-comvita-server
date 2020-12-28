package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.enums.system.TenantConfigTypeEnum;
import com.aquilaflycloud.mdc.param.system.ConfigAddParam;
import com.aquilaflycloud.mdc.param.system.ConfigEditParam;
import com.aquilaflycloud.mdc.param.system.ConfigGetParam;
import com.aquilaflycloud.mdc.param.system.ConfigPageParam;
import com.aquilaflycloud.mdc.result.system.SystemTenantConfigResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * SystemTenantConfigService
 *
 * @author star
 * @date 2020-04-09
 */
public interface SystemTenantConfigService {
    IPage<SystemTenantConfigResult> pageConfig(ConfigPageParam param);

    SystemTenantConfigResult getConfig(ConfigGetParam param);

    void addConfig(ConfigAddParam param);

    void editConfig(ConfigEditParam param);

    SystemTenantConfigResult getConfig(TenantConfigTypeEnum tenantConfigType);
}
