package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.system.SystemAccountConfig;
import com.aquilaflycloud.mdc.param.system.AccountListParam;
import com.aquilaflycloud.mdc.param.system.EasyPayAccountGetParam;
import com.aquilaflycloud.mdc.param.system.EasyPayAccountSaveParam;
import com.aquilaflycloud.mdc.param.system.TencentPositionAccountSaveParam;
import com.aquilaflycloud.mdc.result.system.EasyPayAccountResult;
import com.aquilaflycloud.mdc.result.system.TencentPositionAccountResult;
import com.aquilaflycloud.mdc.service.SystemAccountService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * SystemAccountController
 *
 * @author star
 * @date 2019-12-04
 */
@RestController
@Api(tags = "第三方账号配置接口")
public class SystemAccountController {

    @Resource
    private SystemAccountService systemAccountService;

    @ApiOperation(value = "查询第三方账号列表", notes = "查询第三方账号列表")
    @PreAuthorize("hasAuthority('mdc:system:accountList')")
    @ApiMapping(value = "backend.comvita.system.account.list", method = RequestMethod.POST, permission = true)
    public List<SystemAccountConfig> listAccount(AccountListParam param) {
        return systemAccountService.listAccount(param);
    }

    @ApiOperation(value = "获取腾讯位置服务账号", notes = "获取腾讯位置服务账号")
    @PreAuthorize("hasAuthority('mdc:system:accountGet')")
    @ApiMapping(value = "backend.comvita.system.tencentPositionAccount.get", method = RequestMethod.POST, permission = true)
    public TencentPositionAccountResult getTencentPositionAccount() {
        return systemAccountService.getTencentPositionAccount();
    }

    @ApiOperation(value = "保存腾讯位置服务账号", notes = "保存腾讯位置服务账号")
    @PreAuthorize("hasAuthority('mdc:system:accountSave')")
    @ApiMapping(value = "backend.comvita.system.tencentPositionAccount.save", method = RequestMethod.POST, permission = true)
    public void saveTencentPositionAccount(TencentPositionAccountSaveParam param) {
        systemAccountService.saveTencentPositionAccount(param);
    }
}
