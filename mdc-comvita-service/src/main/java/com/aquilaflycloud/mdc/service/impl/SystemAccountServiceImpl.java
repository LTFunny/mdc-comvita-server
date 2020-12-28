package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.system.AccountSubTypeEnum;
import com.aquilaflycloud.mdc.enums.system.AccountTypeEnum;
import com.aquilaflycloud.mdc.mapper.SystemAccountConfigMapper;
import com.aquilaflycloud.mdc.model.system.SystemAccountConfig;
import com.aquilaflycloud.mdc.param.system.AccountListParam;
import com.aquilaflycloud.mdc.param.system.TencentPositionAccountSaveParam;
import com.aquilaflycloud.mdc.result.system.TencentPositionAccountResult;
import com.aquilaflycloud.mdc.service.SystemAccountService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * SystemAccountServiceImpl
 *
 * @author star
 * @date 2019-12-04
 */
@Service
@DependsOn("springUtil")
public class SystemAccountServiceImpl implements SystemAccountService {
    @Resource
    private SystemAccountConfigMapper systemAccountConfigMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public List<SystemAccountConfig> listAccount(AccountListParam param) {
        return systemAccountConfigMapper.selectList(Wrappers.<SystemAccountConfig>lambdaQuery()
                .eq(param.getAccountType() != null, SystemAccountConfig::getAccountType, param.getAccountType())
                .eq(param.getAccountSubType() != null, SystemAccountConfig::getAccountSubType, param.getAccountSubType())
        );
    }

    private int saveAccount(Object param, AccountTypeEnum accountType, AccountSubTypeEnum accountSubType) {
        SystemAccountConfig systemAccountConfig = systemAccountConfigMapper.selectOne(Wrappers.<SystemAccountConfig>lambdaQuery()
                .eq(SystemAccountConfig::getAccountType, accountType)
                .eq(accountSubType != null, SystemAccountConfig::getAccountSubType, accountSubType)
        );
        int count;
        if (systemAccountConfig == null) {
            systemAccountConfig = new SystemAccountConfig();
            systemAccountConfig.setAccountContent(JSONUtil.toJsonStr(param));
            systemAccountConfig.setAccountType(accountType);
            systemAccountConfig.setAccountSubType(accountSubType);
            count = systemAccountConfigMapper.insert(systemAccountConfig);
        } else {
            SystemAccountConfig update = new SystemAccountConfig();
            update.setId(systemAccountConfig.getId());
            update.setAccountContent(JSONUtil.toJsonStr(param));
            count = systemAccountConfigMapper.updateById(update);
        }
        return count;
    }

    @Override
    public TencentPositionAccountResult getTencentPositionAccount() {
        Long tenantId = MdcUtil.getCurrentTenantId();
        return RedisUtil.valueGet("getTPAccount" + tenantId, () -> {
            SystemAccountConfig systemAccountConfig = systemAccountConfigMapper.selectOne(Wrappers.<SystemAccountConfig>lambdaQuery()
                    .eq(SystemAccountConfig::getAccountType, AccountTypeEnum.TENCENTPOSITION));
            if (systemAccountConfig == null) {
                throw new ServiceException("腾讯位置服务账号不存在");
            }
            TencentPositionAccountResult result = JSONUtil.toBean(systemAccountConfig.getAccountContent(), TencentPositionAccountResult.class);
            result.setId(systemAccountConfig.getId());
            return result;
        });
    }

    @Override
    public void saveTencentPositionAccount(TencentPositionAccountSaveParam param) {
        int count = saveAccount(param, AccountTypeEnum.TENCENTPOSITION, null);
        if (count <= 0) {
            throw new ServiceException("保存腾讯位置服务账号失败");
        }
        Long tenantId = MdcUtil.getCurrentTenantId();
        RedisUtil.redis().delete("getTPAccount" + tenantId);
    }
}
