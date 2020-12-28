package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.IsvAppInfo;
import com.aquilaflycloud.mdc.param.isv.IsvGetParam;
import com.aquilaflycloud.mdc.param.isv.IsvListParam;
import com.aquilaflycloud.mdc.param.isv.IsvSaveParam;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * IsvAppInfoService
 *
 * @author star
 * @date 2019-09-20
 */
public interface IsvAppInfoService {
    IPage<IsvAppInfo> pageIsvInfo(IsvListParam param);

    IsvAppInfo get(IsvGetParam param);

    void save(IsvSaveParam param);

    void delete(IsvGetParam param);
}

