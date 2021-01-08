package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.PreActivityAnalysisResult;
import com.aquilaflycloud.mdc.result.pre.PreActivityDetailResult;
import com.aquilaflycloud.mdc.result.pre.PreActivityPageResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * PreActivityService
 * @author linkq
 */
public interface PreActivityService {
    IPage<PreActivityPageResult> page(PreActivityPageParam param);

    void add(PreActivityAddParam param);

    void update(PreActivityUpdateParam param);

    PreActivityDetailResult get(PreActivityGetParam param);

    void cancel(PreActivityCancelParam param);

    PreActivityAnalysisResult analyse(PreActivityAnalysisParam param);

    IPage<PreActivityPageResult> pagePreActivity(PreActivityPageParam param);
}
