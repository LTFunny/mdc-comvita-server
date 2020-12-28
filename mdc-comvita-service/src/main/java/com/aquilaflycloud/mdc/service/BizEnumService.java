package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.param.system.DictListParam;
import com.aquilaflycloud.mdc.param.system.DictNameParam;

import java.util.Collection;

/**
 * BizEnumService
 *
 * @author star
 * @date 2019-11-16
 */
public interface BizEnumService {
    Collection<Enum> listEnums(DictListParam param);

    void addDataAuth(DictNameParam param);

    void deleteDataAuth(DictNameParam param);
}

