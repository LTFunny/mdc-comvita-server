package com.aquilaflycloud.mdc.service.impl;

import com.aquilaflycloud.mdc.mapper.PreOrderInfoMapper;
import com.aquilaflycloud.mdc.param.pre.AdministrationListParam;
import com.aquilaflycloud.mdc.result.pre.AdministrationPageResult;
import com.aquilaflycloud.mdc.service.PreOrderAdministrationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @Author zly
 */
@Service
public class PreOrderAdministrationServiceImpl implements PreOrderAdministrationService {

    @Resource
    private PreOrderInfoMapper preGoodsInfoMapper;
    @Override
    public IPage<AdministrationPageResult> pageAdministrationList(AdministrationListParam param) {
        IPage<AdministrationPageResult> page=preGoodsInfoMapper.pageAdministrationList(param.page(),param);
        return page;
    }
}
