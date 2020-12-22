package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.system.SystemEnum;
import com.aquilaflycloud.mdc.param.system.EnumAddParam;
import com.aquilaflycloud.mdc.param.system.EnumListParam;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * SystemEnumService
 *
 * @author star
 * @date 2019-11-04
 */
public interface SystemEnumService {
    IPage<SystemEnum> page(EnumListParam param);

    void add(EnumAddParam param);
}

