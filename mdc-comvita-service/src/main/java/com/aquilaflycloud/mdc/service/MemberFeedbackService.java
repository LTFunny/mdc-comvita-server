package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.member.MemberFeedbackInfo;
import com.aquilaflycloud.mdc.param.member.MemberFeedbackAddParam;
import com.aquilaflycloud.mdc.param.member.MemberFeedbackEditParam;
import com.aquilaflycloud.mdc.param.member.MemberFeedbackGetParam;
import com.aquilaflycloud.mdc.param.member.MemberFeedbackPageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * MemberFeedbackService
 *
 * @author zengqingjie
 * @date 2020-06-28
 */
public interface MemberFeedbackService {
    IPage<MemberFeedbackInfo> page(MemberFeedbackPageParam param);

    MemberFeedbackInfo get(MemberFeedbackGetParam param);

    void edit(MemberFeedbackEditParam param);

    void add(MemberFeedbackAddParam param);
}

