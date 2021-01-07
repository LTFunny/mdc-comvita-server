package com.aquilaflycloud.mdc.service;

import cn.hutool.core.date.DateTime;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.member.MemberRewardRecord;
import com.aquilaflycloud.mdc.model.member.MemberRewardRule;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.result.member.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * MemberRewardService
 *
 * @author star
 * @date 2019-11-14
 */
public interface MemberRewardService {
    List<MemberRewardRule> listRewardRule(RewardRuleListParam param);

    void saveRewardSign(RewardSignSaveParam param);

    SignRewardRuleResult getRewardSign(RewardRuleGetParam param);

    void saveRewardScan(RewardScanSaveParam param);

    ScanRewardRuleResult getRewardScan(RewardRuleGetParam param);

    void saveRewardClean(RewardCleanSaveParam param);

    CleanRewardRuleResult getRewardClean(RewardRuleGetParam param);

    void toggleState(RewardRuleIdGetParam param);

    IPage<MemberRewardRecord> page(RewardRecordPageParam param);

    Integer getMemberTotalReward(Long memberId, RewardTypeEnum rewardType);

    void addRecord(RewardRecordAddParam param);

    List<RewardRankResult> listRewardRank(RewardRankListParam param);

    void initRewardRank(RewardRankInitParam param);

    String getRankCacheKey(DateTime start, DateTime end, String appId, RewardTypeEnum rewardType);

    MemberRewardRankResult getRewardRank(MemberRewardRankGetParam param);

    MemberSummaryRewardResult getSummaryReward(MemberSummaryRewardGetParam param);

    IPage<MemberRewardRecord> pageMemberReward(MemberRewardPageParam param);

    List<SignRewardRuleResult> getRewardSignList(String appId);

    Map<RewardTypeEnum, MemberRewardResult> addSignRewardRecord(MemberInfo memberInfo, Integer continueCount);

    Map<RewardTypeEnum, MemberScanRewardResult> addScanRewardRecord(MemberInfo memberInfo, Long formatId, BigDecimal amount, Boolean ignore);

    void addExchangeRewardRecord(MemberInfo memberInfo, RewardTypeEnum rewardType, Integer rewardValue);

    void addLotteryRewardRecord(MemberInfo memberInfo, RewardTypeEnum rewardType, Integer rewardValue);

    void addMissionRewardRecord(MemberInfo memberInfo, RewardTypeEnum rewardType, Integer rewardValue);

    void addOfflineSignRewardRecord(MemberInfo memberInfo, RewardTypeEnum rewardType, Integer rewardValue);

    void addPreActivityRewardRecord(MemberInfo memberInfo, RewardTypeEnum rewardType, Integer rewardValue);

    void addCleanRewardRecord();
}

