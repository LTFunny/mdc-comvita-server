package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.model.member.MemberRewardRecord;
import com.aquilaflycloud.mdc.result.member.RewardRankResult;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberRewardRecordMapper extends AfcBaseMapper<MemberRewardRecord> {
    @InterceptorIgnore(tenantLine = "true")
    List<RewardRankResult> normalSelectRank(@Param(Constants.WRAPPER) Wrapper wrapper);

    List<RewardRankResult> selectRank(@Param(Constants.WRAPPER) Wrapper wrapper);
}
