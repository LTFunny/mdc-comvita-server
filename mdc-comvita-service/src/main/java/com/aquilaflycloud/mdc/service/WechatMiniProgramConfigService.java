package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.wechat.WechatMiniProgramTemplateConfig;
import com.aquilaflycloud.mdc.param.wechat.CodeConfigGetParam;
import com.aquilaflycloud.mdc.param.wechat.CodeConfigListParam;
import com.aquilaflycloud.mdc.param.wechat.CodeConfigSaveParam;

import java.util.List;

/**
 * WechatMiniProgramConfigService
 *
 * @author star
 * @date 2019-10-09
 */
public interface WechatMiniProgramConfigService {

    List<WechatMiniProgramTemplateConfig> list(CodeConfigListParam param);

    WechatMiniProgramTemplateConfig getConfig(CodeConfigGetParam param);

    void deleteConfig(CodeConfigGetParam param);

    void saveConfig(CodeConfigSaveParam param);
}

