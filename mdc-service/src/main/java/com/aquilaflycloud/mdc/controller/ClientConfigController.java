package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.param.member.ClientConfigGetParam;
import com.aquilaflycloud.mdc.param.member.ClientConfigItemGetParam;
import com.aquilaflycloud.mdc.param.member.ClientConfigListParam;
import com.aquilaflycloud.mdc.param.member.ClientConfigSaveParam;
import com.aquilaflycloud.mdc.result.member.ClientConfigResult;
import com.aquilaflycloud.mdc.service.ClientConfigService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClientConfigController
 *
 * @author star
 * @date 2020-04-20
 */
@Api(tags = "会员客户端配置表")
@RestController
public class ClientConfigController {
    @Resource
    private ClientConfigService clientConfigService;

    @ApiOperation(value = "会员客户端配置列表", notes = "会员客户端配置列表")
    @PreAuthorize("hasAuthority('mdc:clientConfig:list')")
    @ApiMapping(value = "backend.comvita.client.config.list", method = RequestMethod.POST, permission = true)
    public List<ClientConfigResult> listConfig(ClientConfigListParam param) {
        return clientConfigService.listConfig(param);
    }

    @ApiOperation(value = "保存会员客户端配置", notes = "保存会员客户端配置")
    @PreAuthorize("hasAuthority('mdc:clientConfig:save')")
    @ApiMapping(value = "backend.comvita.client.config.save", method = RequestMethod.POST, permission = true)
    public void saveConfig(ClientConfigSaveParam param) {
        clientConfigService.saveConfig(param);
    }

    @ApiOperation(value = "获取会员客户端配置", notes = "获取会员客户端配置")
    @PreAuthorize("hasAuthority('mdc:clientConfig:get')")
    @ApiMapping(value = "backend.comvita.client.config.get", method = RequestMethod.POST, permission = true)
    public ClientConfigResult getConfig(ClientConfigGetParam param) {
        return clientConfigService.getConfig(param);
    }

    @ApiOperation(value = "获取会员客户端配置值", notes = "获取会员客户端配置值")
    @PreAuthorize("hasAuthority('mdc:clientConfig:get')")
    @ApiMapping(value = "backend.comvita.client.configItem.get", method = RequestMethod.POST, permission = true)
    public BaseResult getConfigItem(ClientConfigItemGetParam param) {
        return clientConfigService.getConfigItem(param);
    }
}
