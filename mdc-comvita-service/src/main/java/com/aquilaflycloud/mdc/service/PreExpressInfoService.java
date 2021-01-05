package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.pre.PreExpressInfo;
import com.aquilaflycloud.mdc.param.pre.PreExpressInfoPageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author zengqingjie
 * @Date 2021-01-05
 */
public interface PreExpressInfoService {
    IPage<PreExpressInfo> page(PreExpressInfoPageParam param);
}
