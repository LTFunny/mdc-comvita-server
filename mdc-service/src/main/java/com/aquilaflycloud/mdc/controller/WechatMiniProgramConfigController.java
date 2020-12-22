package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.wechat.WechatMiniProgramTemplateConfig;
import com.aquilaflycloud.mdc.param.wechat.CodeConfigGetParam;
import com.aquilaflycloud.mdc.param.wechat.CodeConfigListParam;
import com.aquilaflycloud.mdc.param.wechat.CodeConfigSaveParam;
import com.aquilaflycloud.mdc.service.WechatMiniProgramConfigService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * WechatMiniProgramConfigController
 *
 * @author star
 * @date 2019-10-09
 */
@Api(tags = "微信小程序代码模板配置接口")
@RestController
public class WechatMiniProgramConfigController {

    @Resource
    private WechatMiniProgramConfigService wechatMiniProgramConfigService;

    @ApiOperation(value = "查询小程序代码模板配置列表", notes = "查询小程序代码模板配置列表")
    @PreAuthorize("hasAuthority('mdc:wechat:codeConfigList')")
    @ApiMapping(value = "backend.mdc.wechat.codeConfig.list", method = RequestMethod.POST, permission = true)
    public List<WechatMiniProgramTemplateConfig> list(CodeConfigListParam param) {
        return wechatMiniProgramConfigService.list(param);
    }

    @ApiOperation(value = "查询小程序代码模板配置", notes = "查询小程序代码模板配置")
    @PreAuthorize("hasAuthority('mdc:wechat:codeConfigList')")
    @ApiMapping(value = "backend.mdc.wechat.codeConfig.get", method = RequestMethod.POST, permission = true)
    public WechatMiniProgramTemplateConfig getConfig(CodeConfigGetParam param) {
        return wechatMiniProgramConfigService.getConfig(param);
    }

    @ApiOperation(value = "删除小程序代码模板配置", notes = "删除小程序代码模板配置")
    @PreAuthorize("hasAuthority('mdc:wechat:codeConfigDelete')")
    @ApiMapping(value = "backend.mdc.wechat.codeConfig.delete", method = RequestMethod.POST, permission = true)
    public void deleteConfig(CodeConfigGetParam param) {
        wechatMiniProgramConfigService.deleteConfig(param);
    }

    @ApiOperation(value = "保存小程序代码模板配置", notes = "保存小程序代码模板配置")
    @PreAuthorize("hasAuthority('mdc:wechat:codeConfigSave')")
    @ApiMapping(value = "backend.mdc.wechat.codeConfig.save", method = RequestMethod.POST, permission = true)
    public void saveConfig(CodeConfigSaveParam param) {
        wechatMiniProgramConfigService.saveConfig(param);
    }

}
