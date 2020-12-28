package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.system.SystemAccountConfig;
import com.aquilaflycloud.mdc.param.system.*;
import com.aquilaflycloud.mdc.result.system.EasyPayAccountResult;
import com.aquilaflycloud.mdc.result.system.TencentPositionAccountResult;

import java.util.List;

/**
 * SystemAccountService
 *
 * @author star
 * @date 2019-12-04
 */
public interface SystemAccountService {
    List<SystemAccountConfig> listAccount(AccountListParam param);

    TencentPositionAccountResult getTencentPositionAccount();

    void saveTencentPositionAccount(TencentPositionAccountSaveParam param);
}

