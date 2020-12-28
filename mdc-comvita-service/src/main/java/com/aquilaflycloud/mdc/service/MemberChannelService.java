package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.result.member.RegisterChannelAnalysisResult;
import com.aquilaflycloud.mdc.result.member.RegisterChannelResult;
import com.aquilaflycloud.mdc.result.member.RegisterChannelStatisticsResult;
import com.aquilaflycloud.mdc.result.member.RegisterChannelTopResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * MemberChannelService
 *
 * @author star
 * @date 2020-02-19
 */
public interface MemberChannelService {
    IPage<RegisterChannelResult> pageRegisterChannel(RegisterChannelPageParam param);

    void addRegisterChannel(RegisterChannelAddParam param);

    void batchAddRegisterChannel(RegisterChannelBatchAddParam param);

    void editRegisterChannel(RegisterChannelEditParam param);

    void toggleRegisterChannel(RegisterChannelGetParam param);

    BaseResult<String> downloadChannelMiniCode(RegisterChannelBatchGetParam param);

    RegisterChannelStatisticsResult getChannelStatistics(RegisterChannelTimeGetParam param);

    List<RegisterChannelAnalysisResult> getChannelAnalysis(RegisterChannelTimeGetParam param);

    List<RegisterChannelTopResult> listChannelTop(RegisterChannelTopListParam param);

    void addChannelRel(RegisterChannelGetParam param);
}

