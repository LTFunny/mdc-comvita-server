package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.mapper.IsvAppInfoMapper;
import com.aquilaflycloud.mdc.model.IsvAppInfo;
import com.aquilaflycloud.mdc.param.isv.IsvGetParam;
import com.aquilaflycloud.mdc.param.isv.IsvListParam;
import com.aquilaflycloud.mdc.param.isv.IsvSaveParam;
import com.aquilaflycloud.mdc.service.IsvAppInfoService;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * IsvAppInfoServiceImpl
 *
 * @author star
 * @date 2019-09-20
 */
@Service
public class IsvAppInfoServiceImpl implements IsvAppInfoService {
    @Resource
    private IsvAppInfoMapper isvAppInfoMapper;

    @Override
    public IPage<IsvAppInfo> pageIsvInfo(IsvListParam param) {
        return isvAppInfoMapper.selectPage(param.page(), Wrappers.<IsvAppInfo>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getAppKey()), IsvAppInfo::getAppKey, param.getAppKey())
                .orderByDesc(IsvAppInfo::getId)
        );
    }

    @Override
    public IsvAppInfo get(IsvGetParam param) {
        return isvAppInfoMapper.selectOne(Wrappers.<IsvAppInfo>lambdaQuery().eq(IsvAppInfo::getAppKey, param.getAppKey()));
    }

    @Override
    public void save(IsvSaveParam param) {
        IsvAppInfo isvAppInfo = isvAppInfoMapper.selectOne(Wrappers.<IsvAppInfo>lambdaQuery().eq(IsvAppInfo::getAppKey, param.getAppKey()));
        int count;
        if (isvAppInfo == null) {
            isvAppInfo = new IsvAppInfo();
            BeanUtil.copyProperties(param, isvAppInfo);
            count = isvAppInfoMapper.insert(isvAppInfo);
        } else {
            IsvAppInfo update = new IsvAppInfo();
            BeanUtil.copyProperties(param, update);
            update.setId(isvAppInfo.getId());
            count = isvAppInfoMapper.updateById(update);
        }
        if (count > 0) {
            RedisUtil.redis().delete("getIsv" + param.getAppKey());
        } else {
            throw new ServiceException("保存isv失败");
        }
    }

    @Override
    public void delete(IsvGetParam param) {
        int count = isvAppInfoMapper.delete(Wrappers.<IsvAppInfo>lambdaQuery().eq(IsvAppInfo::getAppKey, param.getAppKey()));
        if (count > 0) {
            RedisUtil.redis().delete("getIsv" + param.getAppKey());
        } else {
            throw new ServiceException("删除isv失败");
        }
    }
}
