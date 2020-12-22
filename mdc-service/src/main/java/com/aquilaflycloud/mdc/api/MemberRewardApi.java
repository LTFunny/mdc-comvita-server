package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.model.member.MemberRewardRecord;
import com.aquilaflycloud.mdc.param.member.MemberRewardPageParam;
import com.aquilaflycloud.mdc.param.member.MemberRewardRankGetParam;
import com.aquilaflycloud.mdc.param.member.MemberSummaryRewardGetParam;
import com.aquilaflycloud.mdc.result.member.MemberRewardRankResult;
import com.aquilaflycloud.mdc.result.member.MemberSummaryRewardResult;
import com.aquilaflycloud.mdc.service.MemberRewardService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * MemberRewardApi
 *
 * @author star
 * @date 2019-12-31
 */
@RestController
@Api(tags = "会员奖励相关接口")
public class MemberRewardApi {

    @Resource
    private MemberRewardService memberRewardService;

    @ApiOperation(value = "查询会员奖励排名", notes = "查询会员奖励排名")
    @ApiMapping(value = "mdc.reward.rank.get", method = RequestMethod.POST)
    public MemberRewardRankResult getRewardRank(MemberRewardRankGetParam param) {
        return memberRewardService.getRewardRank(param);
    }

    @ApiOperation(value = "获取会员奖励汇总", notes = "获取会员奖励汇总")
    @ApiMapping(value = "mdc.reward.summary.get", method = RequestMethod.POST)
    public MemberSummaryRewardResult getSummaryReward(MemberSummaryRewardGetParam param) {
        return memberRewardService.getSummaryReward(param);
    }

    @ApiOperation(value = "查询会员奖励记录列表", notes = "查询会员奖励记录列表")
    @ApiMapping(value = "mdc.reward.record.page", method = RequestMethod.POST)
    public IPage<MemberRewardRecord> pageMemberReward(MemberRewardPageParam param) {
        return memberRewardService.pageMemberReward(param);
    }
}
