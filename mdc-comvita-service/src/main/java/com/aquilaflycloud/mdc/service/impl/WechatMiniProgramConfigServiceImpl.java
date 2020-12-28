package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.mapper.WechatMiniProgramTemplateConfigMapper;
import com.aquilaflycloud.mdc.model.wechat.WechatMiniProgramTemplateConfig;
import com.aquilaflycloud.mdc.param.wechat.CodeConfigGetParam;
import com.aquilaflycloud.mdc.param.wechat.CodeConfigListParam;
import com.aquilaflycloud.mdc.param.wechat.CodeConfigSaveParam;
import com.aquilaflycloud.mdc.service.WechatMiniProgramConfigService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * WechatMiniProgramConfigServiceImpl
 *
 * @author star
 * @date 2019-10-09
 */
@Service
public class WechatMiniProgramConfigServiceImpl implements WechatMiniProgramConfigService {
    @Resource
    private WechatMiniProgramTemplateConfigMapper wechatMiniProgramTemplateConfigMapper;

    @Override
    public List<WechatMiniProgramTemplateConfig> list(CodeConfigListParam param) {
        LambdaQueryWrapper<WechatMiniProgramTemplateConfig> wrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(param.getTemplateConfigName())) {
            wrapper.like(WechatMiniProgramTemplateConfig::getTemplateConfigName, param.getTemplateConfigName());
        }
        if (StrUtil.isNotBlank(param.getUserDesc())) {
            wrapper.like(WechatMiniProgramTemplateConfig::getUserDesc, param.getUserDesc());
        }
        if (StrUtil.isNotBlank(param.getTemplateType())) {
            wrapper.eq(WechatMiniProgramTemplateConfig::getTemplateType, param.getTemplateType());
        }
        return wechatMiniProgramTemplateConfigMapper.selectList(wrapper);
    }

    @Override
    public WechatMiniProgramTemplateConfig getConfig(CodeConfigGetParam param) {
        return wechatMiniProgramTemplateConfigMapper.selectById(param.getId());
    }

    @Override
    @Transactional
    public void deleteConfig(CodeConfigGetParam param) {
        int count = wechatMiniProgramTemplateConfigMapper.deleteById(param.getId());
        if (count <= 0) {
            throw new ServiceException("删除失败");
        }
    }

    @Override
    @Transactional
    public void saveConfig(CodeConfigSaveParam param) {
        WechatMiniProgramTemplateConfig template = new WechatMiniProgramTemplateConfig();
        BeanUtil.copyProperties(param, template);
        int count;
        if (template.getId() != null) {
            count = wechatMiniProgramTemplateConfigMapper.updateById(template);
        } else {
            count = wechatMiniProgramTemplateConfigMapper.insert(template);
        }
        if (count <= 0) {
            throw new ServiceException("保存失败");
        }
    }

}
