package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.param.member.MemberSignAnalysisParam;
import com.aquilaflycloud.mdc.param.member.MemberSignContinueAnalysisParam;
import com.aquilaflycloud.mdc.param.member.MemberSignPageParam;
import com.aquilaflycloud.mdc.result.member.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * MemberSignService
 *
 * @author star
 * @date 2020-01-02
 */
public interface MemberSignService {
    IPage<MemberSignInfoResult> pageMemberRecord(MemberSignPageParam param);

    IPage<MemberSignResult> pageRecord(MemberSignPageParam param);

    List<MemberSignAnalysisResult> getSignCountAnalysis(MemberSignAnalysisParam param);

    List<MemberSignContinueAnalysisResult> getContinueSignCountAnalysis(MemberSignContinueAnalysisParam param);

    MemberSignDescInfoResult getSignInfo();

    MemberSignAddResult addSignInfo();
}

