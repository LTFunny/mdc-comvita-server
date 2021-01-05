package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.mapper.PreExpressInfoMapper;
import com.aquilaflycloud.mdc.model.pre.PreExpressInfo;
import com.aquilaflycloud.mdc.param.pre.PreExpressInfoPageParam;
import com.aquilaflycloud.mdc.service.PreExpressInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * PreExpressInfoServiceImpl
 *
 * @author zengqingjie
 * @date 2021-01-04
 */
@Service
@Slf4j
public class PreExpressInfoServiceImpl implements PreExpressInfoService {
    @Resource
    private PreExpressInfoMapper preExpressInfoMapper;

    @Override
    public IPage<PreExpressInfo> page(PreExpressInfoPageParam param) {
        return preExpressInfoMapper.normalSelectPage(param.page(), Wrappers.<PreExpressInfo> lambdaQuery()
                .like(StrUtil.isNotBlank(param.getExpressName()), PreExpressInfo::getExpressName, param.getExpressName())
                .orderByAsc(PreExpressInfo::getSort)
        );
    }
}
