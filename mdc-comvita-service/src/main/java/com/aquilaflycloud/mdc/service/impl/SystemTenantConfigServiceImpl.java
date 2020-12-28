package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.system.TenantConfigTypeEnum;
import com.aquilaflycloud.mdc.mapper.SystemTenantConfigMapper;
import com.aquilaflycloud.mdc.model.system.SystemTenantConfig;
import com.aquilaflycloud.mdc.param.system.ConfigAddParam;
import com.aquilaflycloud.mdc.param.system.ConfigEditParam;
import com.aquilaflycloud.mdc.param.system.ConfigGetParam;
import com.aquilaflycloud.mdc.param.system.ConfigPageParam;
import com.aquilaflycloud.mdc.result.system.*;
import com.aquilaflycloud.mdc.service.SystemTenantConfigService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * SystemTenantConfigServiceImpl
 *
 * @author star
 * @date 2020-04-09
 */
@Service
public class SystemTenantConfigServiceImpl implements SystemTenantConfigService {
    @Resource
    private SystemTenantConfigMapper systemTenantConfigMapper;

    private SystemTenantConfigResult parseResult(SystemTenantConfig config) {
        if (config == null || config.getConfigType() == null) {
            return null;
        }
        SystemTenantConfigResult result = Convert.convert(SystemTenantConfigResult.class, config);
        switch (config.getConfigType()) {
            case WECHATDIRECTPAY: {
                WechatDirectPayConfig wechatDirectPayConfig = JSONUtil.toBean(config.getConfigValue(), WechatDirectPayConfig.class);
                result.setWechatDirectPayConfig(wechatDirectPayConfig);
                break;
            }
            case AUTHORUNIVERSAL: {
                AuthorUniversalConfig authorUniversalConfig = JSONUtil.toBean(config.getConfigValue(), AuthorUniversalConfig.class);
                result.setAuthorUniversalConfig(authorUniversalConfig);
                break;
            }
            case UNIFIEDMEMBER: {
                UnifiedMemberConfig unifiedMemberConfig = JSONUtil.toBean(config.getConfigValue(), UnifiedMemberConfig.class);
                result.setUnifiedMemberConfig(unifiedMemberConfig);
                break;
            }
            default:
        }
        return result;
    }

    @Override
    public IPage<SystemTenantConfigResult> pageConfig(ConfigPageParam param) {
        return systemTenantConfigMapper.selectPage(param.page(), Wrappers.<SystemTenantConfig>lambdaQuery()
                .eq(param.getConfigType() != null, SystemTenantConfig::getConfigType, param.getConfigType())
        ).convert(this::parseResult);
    }

    @Override
    public SystemTenantConfigResult getConfig(ConfigGetParam param) {
        SystemTenantConfig config = systemTenantConfigMapper.selectById(param.getId());
        return parseResult(config);
    }

    @Override
    public void addConfig(ConfigAddParam param) {
        int count = systemTenantConfigMapper.selectCount(Wrappers.<SystemTenantConfig>lambdaQuery()
                .eq(SystemTenantConfig::getConfigType, param.getConfigType())
        );
        if (count > 0) {
            throw new ServiceException("该配置已存在");
        }
        SystemTenantConfig config = new SystemTenantConfig();
        BeanUtil.copyProperties(param, config);
        switch (param.getConfigType()) {
            case WECHATDIRECTPAY: {
                WechatDirectPayConfig wechatDirectPayConfig = new WechatDirectPayConfig();
                BeanUtil.copyProperties(param.getWechatDirectPayConfig(), wechatDirectPayConfig);
                wechatDirectPayConfig.setEffective(BooleanUtil.isTrue(wechatDirectPayConfig.getEffective()));
                config.setConfigValue(JSONUtil.toJsonStr(wechatDirectPayConfig));
                break;
            }
            case AUTHORUNIVERSAL: {
                AuthorUniversalConfig authorUniversalConfig = new AuthorUniversalConfig();
                BeanUtil.copyProperties(param.getAuthorUniversalConfig(), authorUniversalConfig);
                authorUniversalConfig.setVisible(BooleanUtil.isTrue(authorUniversalConfig.getVisible()));
                config.setConfigValue(JSONUtil.toJsonStr(authorUniversalConfig));
                break;
            }
            case UNIFIEDMEMBER: {
                UnifiedMemberConfig unifiedMemberConfig = new UnifiedMemberConfig();
                BeanUtil.copyProperties(param.getUnifiedMemberConfig(), unifiedMemberConfig);
                unifiedMemberConfig.setUnified(BooleanUtil.isTrue(unifiedMemberConfig.getUnified()));
                config.setConfigValue(JSONUtil.toJsonStr(unifiedMemberConfig));
            }
            default:
        }
        count = systemTenantConfigMapper.insert(config);
        if (count <= 0) {
            throw new ServiceException("新增配置失败");
        }
        RedisUtil.redis().delete(getKey(config.getConfigType()));
    }

    @Override
    public void editConfig(ConfigEditParam param) {
        SystemTenantConfig config = systemTenantConfigMapper.selectById(param.getId());
        if (config == null) {
            throw new ServiceException("该配置不存在");
        }
        SystemTenantConfig update = new SystemTenantConfig();
        BeanUtil.copyProperties(param, update);
        switch (config.getConfigType()) {
            case WECHATDIRECTPAY: {
                WechatDirectPayConfig wechatDirectPayConfig = JSONUtil.toBean(config.getConfigValue(), WechatDirectPayConfig.class);
                BeanUtil.copyProperties(param.getWechatDirectPayConfig(), wechatDirectPayConfig, CopyOptions.create().ignoreNullValue());
                update.setConfigValue(JSONUtil.toJsonStr(wechatDirectPayConfig));
                break;
            }
            case AUTHORUNIVERSAL: {
                AuthorUniversalConfig authorUniversalConfig = JSONUtil.toBean(config.getConfigValue(), AuthorUniversalConfig.class);
                BeanUtil.copyProperties(param.getAuthorUniversalConfig(), authorUniversalConfig, CopyOptions.create().ignoreNullValue());
                update.setConfigValue(JSONUtil.toJsonStr(authorUniversalConfig));
                break;
            }
            case UNIFIEDMEMBER: {
                UnifiedMemberConfig unifiedMemberConfig = JSONUtil.toBean(config.getConfigValue(), UnifiedMemberConfig.class);
                BeanUtil.copyProperties(param.getUnifiedMemberConfig(), unifiedMemberConfig, CopyOptions.create().ignoreNullValue());
                update.setConfigValue(JSONUtil.toJsonStr(unifiedMemberConfig));
                break;
            }
            default:
        }
        int count = systemTenantConfigMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("编辑配置失败");
        }
        RedisUtil.redis().delete(getKey(config.getConfigType()));
    }

    private String getKey(TenantConfigTypeEnum tenantConfigType) {
        return StrUtil.join("_", "tenantConfig", tenantConfigType.getType(), MdcUtil.getCurrentTenantId());
    }

    @Override
    public SystemTenantConfigResult getConfig(TenantConfigTypeEnum tenantConfigType) {
        String key = getKey(tenantConfigType);
        String lockName = key + "Lock";
        SystemTenantConfig systemTenantConfig = RedisUtil.syncValueGet(lockName, key, 7, ()->{
            SystemTenantConfig config = systemTenantConfigMapper.selectOne(Wrappers.<SystemTenantConfig>lambdaQuery()
                    .eq(SystemTenantConfig::getConfigType, tenantConfigType)
            );
            if (config == null) {
                config = new SystemTenantConfig();
            }
            return config;
        });
        return parseResult(systemTenantConfig);
    }
}
