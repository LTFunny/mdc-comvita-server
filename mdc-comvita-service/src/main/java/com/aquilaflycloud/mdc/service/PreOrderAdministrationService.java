package com.aquilaflycloud.mdc.service;


import com.aquilaflycloud.mdc.param.pre.AdministrationListParam;
import com.aquilaflycloud.mdc.result.pre.AdministrationPageResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author zly
 */
public interface PreOrderAdministrationService {

    IPage<AdministrationPageResult> pageAdministrationList(AdministrationListParam param);


}
