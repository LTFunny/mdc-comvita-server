package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.param.system.ConfigAddParam;
import com.aquilaflycloud.mdc.param.system.ConfigEditParam;
import com.aquilaflycloud.mdc.param.system.ConfigGetParam;
import com.aquilaflycloud.mdc.param.system.ConfigPageParam;
import com.aquilaflycloud.mdc.result.system.SystemTenantConfigResult;
import com.aquilaflycloud.mdc.service.SystemTenantConfigService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * SystemTenantConfigController
 *
 * @author star
 * @date 2020-04-09
 */
@RestController
@Api(tags = "租户配置管理")
public class SystemTenantConfigController {
    @Resource
    private SystemTenantConfigService systemTenantConfigService;

    @ApiOperation(value = "获取配置列表(分页)", notes = "获取配置列表(分页)")
    @PreAuthorize("hasAuthority('mdc:config:list')")
    @ApiMapping(value = "backend.comvita.system.config.page", method = RequestMethod.POST, permission = true)
    public IPage<SystemTenantConfigResult> pageConfig(ConfigPageParam param) {
        return systemTenantConfigService.pageConfig(param);
    }

    @ApiOperation(value = "获取配置详情", notes = "获取配置详情")
    @PreAuthorize("hasAuthority('mdc:config:get')")
    @ApiMapping(value = "backend.comvita.system.config.get", method = RequestMethod.POST, permission = true)
    public SystemTenantConfigResult getConfig(ConfigGetParam param) {
        return systemTenantConfigService.getConfig(param);
    }

    @ApiOperation(value = "新增配置", notes = "新增配置")
    @PreAuthorize("hasAuthority('mdc:config:add')")
    @ApiMapping(value = "backend.comvita.system.config.add", method = RequestMethod.POST, permission = true)
    public void addConfig(ConfigAddParam param) {
        systemTenantConfigService.addConfig(param);
    }

    @ApiOperation(value = "编辑配置", notes = "编辑配置")
    @PreAuthorize("hasAuthority('mdc:config:edit')")
    @ApiMapping(value = "backend.comvita.system.config.edit", method = RequestMethod.POST, permission = true)
    public void editConfig(ConfigEditParam param) {
        systemTenantConfigService.editConfig(param);
    }
}
