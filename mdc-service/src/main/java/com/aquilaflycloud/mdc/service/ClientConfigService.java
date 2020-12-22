package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.result.member.ClientConfigResult;

import java.io.Serializable;
import java.util.List;

/**
 * ClientConfigService
 *
 * @author star
 * @date 2020-04-20
 */
public interface ClientConfigService {
    List<ClientConfigResult> listConfig(ClientConfigListParam param);

    void saveConfig(ClientConfigSaveParam param);

    ClientConfigResult getConfig(ClientConfigGetParam param);

    BaseResult getConfigItem(ClientConfigItemGetParam param);

    BaseResult getItem(ClientItemGetParam param);

    <T extends Serializable> T getItemByName(String appId, String itemName, Long tenantId);

    <T extends Serializable> T getItemByName(String appId, String itemName);
}

