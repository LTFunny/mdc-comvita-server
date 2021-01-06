package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.PreActivityAnalysisResult;
import com.aquilaflycloud.mdc.result.pre.PreActivityDetailResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * PreActivityService
 * @author linkq
 */
public interface PreActivityService {
    IPage<PreActivityInfo> page(PreActivityPageParam param);

    void add(PreActivityAddParam param);

    void update(PreActivityUpdateParam param);

    PreActivityDetailResult get(PreActivityGetParam param);

    void cancel(PreActivityCancelParam param);

    PreActivityAnalysisResult analyse(PreActivityAnalysisParam param);
}
