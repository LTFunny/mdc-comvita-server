package com.aquilaflycloud.mdc.service;


import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.aquilaflycloud.mdc.param.pre.AdministrationListParam;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author zly
 */
public interface PreOrderAdministrationService {

    IPage<PreOrderInfo> pageAdministrationList(AdministrationListParam param);


}
