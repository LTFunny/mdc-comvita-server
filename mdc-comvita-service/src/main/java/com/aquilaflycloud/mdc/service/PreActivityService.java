package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * PreActivityService
 * @author linkq
 */
public interface PreActivityService {
    IPage<PreActivityPageApiResult> pagePreActivity(PreActivityPageParam param);

    PreActivityInfoApiResult getPreActivity(PreActivityGetParam param);

    IPage<PreActivityPageResult> page(PreActivityPageParam param);

    void add(PreActivityAddParam param);

    void update(PreActivityUpdateParam param);

    PreActivityDetailResult get(PreActivityGetParam param);

    void changeState(PreActivityCancelParam param);

    PreActivityAnalysisResult analyse(PreActivityAnalysisParam param);
}
