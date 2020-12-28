package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.richtext.RichtextDescriptionInfo;
import com.aquilaflycloud.mdc.param.richtext.RichtextDescriptionEditParam;
import com.aquilaflycloud.mdc.param.richtext.RichtextDescriptionGetParam;

/**
 * RichtextDescriptionService
 *
 * @author zengqingjie
 * @date 2020-06-23
 */
public interface RichtextDescriptionService {
    RichtextDescriptionInfo getByType(RichtextDescriptionGetParam param);

    RichtextDescriptionInfo edit(RichtextDescriptionEditParam param);
}

