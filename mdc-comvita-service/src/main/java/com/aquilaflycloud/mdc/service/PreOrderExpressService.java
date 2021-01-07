package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.pre.PreOrderExpress;
import com.aquilaflycloud.mdc.param.pre.PreOrderExpressInfoParam;
import com.aquilaflycloud.mdc.param.pre.PreOrderInfoGetParam;
import com.aquilaflycloud.mdc.result.pre.PreOrderExpressResult;

import java.util.List;

/**
 * @Author zengqingjie
 * @Date 2021-01-04
 */
public interface PreOrderExpressService {
    List<PreOrderExpressResult> queryTrackInfo(PreOrderExpressInfoParam param);

    PreOrderExpress orderExpressGetInfo(PreOrderInfoGetParam param);
}
