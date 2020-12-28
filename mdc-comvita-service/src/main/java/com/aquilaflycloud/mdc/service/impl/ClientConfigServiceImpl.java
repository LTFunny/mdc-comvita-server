package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.mapper.MemberClientConfigMapper;
import com.aquilaflycloud.mdc.model.member.MemberClientConfig;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.result.member.ClientConfigResult;
import com.aquilaflycloud.mdc.service.ClientConfigService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClientConfigServiceImpl
 *
 * @author star
 * @date 2020-04-20
 */
@Service
public class ClientConfigServiceImpl implements ClientConfigService {
    @Resource
    private MemberClientConfigMapper memberClientConfigMapper;

    @Override
    public List<ClientConfigResult> listConfig(ClientConfigListParam param) {
        return memberClientConfigMapper.selectList(Wrappers.<MemberClientConfig>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getAppId()), MemberClientConfig::getAppId, param.getAppId())
                .orderByDesc(MemberClientConfig::getCreateTime)
        ).stream().map(config -> {
            ClientConfigResult result = JSONUtil.toBean(config.getConfigContent(), ClientConfigResult.class);
            result.setId(config.getId());
            return result;
        }).collect(Collectors.toList());
    }

    private ClientConfigResult initClientConfig(String appId, Long tenantId) {
        String key = StrUtil.join("_", "getClientConfig", StrUtil.emptyIfNull(appId), tenantId);
        RedisUtil.redis().delete(key);
        return RedisUtil.valueGet(key, () -> {
            MemberClientConfig config = memberClientConfigMapper.selectOne(Wrappers.<MemberClientConfig>lambdaQuery()
                    .eq(StrUtil.isNotBlank(appId), MemberClientConfig::getAppId, appId)
                    .isNull(StrUtil.isBlank(appId), MemberClientConfig::getAppId)
                    .orderByDesc(MemberClientConfig::getCreateTime)
                    .last("limit 1"));
            ClientConfigResult configResult;
            if (config != null && StrUtil.isNotBlank(config.getConfigContent())) {
                configResult = JSONUtil.toBean(config.getConfigContent(), ClientConfigResult.class);
            } else {
                configResult = new ClientConfigResult();
            }
            return configResult;
        });
    }

    @Override
    public void saveConfig(ClientConfigSaveParam param) {
        MemberClientConfig config = memberClientConfigMapper.selectOne(Wrappers.<MemberClientConfig>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getAppId()), MemberClientConfig::getAppId, param.getAppId())
                .orderByDesc(MemberClientConfig::getCreateTime)
                .last("limit 1")
        );
        int count;
        if (config == null) {
            config = new MemberClientConfig();
            config.setAppId(param.getAppId());
            config.setConfigContent(JSONUtil.toJsonStr(param));
            count = memberClientConfigMapper.insert(config);
        } else {
            ClientConfigResult result = JSONUtil.toBean(config.getConfigContent(), ClientConfigResult.class);
            BeanUtil.copyProperties(param, result, CopyOptions.create().ignoreNullValue());
            MemberClientConfig update = new MemberClientConfig();
            update.setId(config.getId());
            update.setConfigContent(JSONUtil.toJsonStr(result));
            count = memberClientConfigMapper.updateById(update);
        }
        if (count <= 0) {
            throw new ServiceException("保存客户端配置失败");
        }
        initClientConfig(config.getAppId(), MdcUtil.getCurrentTenantId());
    }

    @Override
    public ClientConfigResult getConfig(ClientConfigGetParam param) {
        MemberClientConfig config = memberClientConfigMapper.selectById(param.getId());
        if (config != null) {
            ClientConfigResult result = JSONUtil.toBean(config.getConfigContent(), ClientConfigResult.class);
            result.setId(config.getId());
            return result;
        }
        return null;
    }

    @Override
    public BaseResult getConfigItem(ClientConfigItemGetParam param) {
        MemberClientConfig config = memberClientConfigMapper.selectOne(Wrappers.<MemberClientConfig>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getAppId()), MemberClientConfig::getAppId, param.getAppId())
                .orderByDesc(MemberClientConfig::getCreateTime)
                .last("limit 1")
        );
        Object value = null;
        if (config != null) {
            ClientConfigResult result = JSONUtil.toBean(config.getConfigContent(), ClientConfigResult.class);
            DynaBean bean = DynaBean.create(result);
            value = bean.safeGet(param.getItemName());
        }
        return new BaseResult().setResult(value);
    }

    @Override
    public BaseResult getItem(ClientItemGetParam param) {
        return new BaseResult().setResult(getItemByName(MdcUtil.getOtherAppId(), param.getItemName()));
    }

    @Override
    public <T extends Serializable> T getItemByName(String appId, String itemName, Long tenantId) {
        String key = StrUtil.join("_", "getClientConfig", appId, tenantId);
        ClientConfigResult result = RedisUtil.<ClientConfigResult>valueRedis().get(key);
        if (result == null) {
            result = initClientConfig(appId, tenantId);
        }
        if (result == null || BeanUtil.isEmpty(result)) {
            key = StrUtil.join("_", "getClientConfig", "", tenantId);
            result = RedisUtil.<ClientConfigResult>valueRedis().get(key);
        }
        if (result == null || BeanUtil.isEmpty(result)) {
            result = initClientConfig(null, tenantId);
        }
        DynaBean bean = DynaBean.create(result);
        return bean.safeGet(itemName);
    }

    @Override
    public <T extends Serializable> T getItemByName(String appId, String itemName) {
        return getItemByName(appId, itemName, MdcUtil.getCurrentTenantId());
    }
}
