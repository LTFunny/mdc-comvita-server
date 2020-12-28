package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.member.MemberRewardRecord;
import com.aquilaflycloud.mdc.model.member.MemberRewardRule;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.result.member.*;
import com.aquilaflycloud.mdc.service.MemberRewardService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * MemberRewardController
 *
 * @author star
 * @date 2019-11-14
 */
@RestController
@Api(tags = "会员奖励接口")
public class MemberRewardController {

    @Resource
    private MemberRewardService memberRewardService;

    @ApiOperation(value = "查询多个奖励规则", notes = "查询多个奖励规则")
    @PreAuthorize("hasAuthority('mdc:reward:ruleList')")
    @ApiMapping(value = "backend.comvita.reward.rule.list", method = RequestMethod.POST, permission = true)
    public List<MemberRewardRule> listRewardRule(RewardRuleListParam param) {
        return memberRewardService.listRewardRule(param);
    }

    @ApiOperation(value = "保存签到奖励规则", notes = "保存签到奖励规则")
    @PreAuthorize("hasAuthority('mdc:reward:ruleSave')")
    @ApiMapping(value = "backend.comvita.reward.sign.save", method = RequestMethod.POST, permission = true)
    public void saveRewardSign(RewardSignSaveParam param) {
        memberRewardService.saveRewardSign(param);
    }

    @ApiOperation(value = "获取签到奖励规则", notes = "获取签到奖励规则")
    @PreAuthorize("hasAuthority('mdc:reward:ruleGet')")
    @ApiMapping(value = "backend.comvita.reward.sign.get", method = RequestMethod.POST, permission = true)
    public SignRewardRuleResult getRewardSign(RewardRuleGetParam param) {
        return memberRewardService.getRewardSign(param);
    }

    @ApiOperation(value = "保存消费奖励规则", notes = "保存消费奖励规则")
    @PreAuthorize("hasAuthority('mdc:reward:ruleSave')")
    @ApiMapping(value = "backend.comvita.reward.scan.save", method = RequestMethod.POST, permission = true)
    public void saveRewardScan(RewardScanSaveParam param) {
        memberRewardService.saveRewardScan(param);
    }

    @ApiOperation(value = "获取消费奖励规则", notes = "获取消费奖励规则")
    @PreAuthorize("hasAuthority('mdc:reward:ruleGet')")
    @ApiMapping(value = "backend.comvita.reward.scan.get", method = RequestMethod.POST, permission = true)
    public ScanRewardRuleResult getRewardScan(RewardRuleGetParam param) {
        return memberRewardService.getRewardScan(param);
    }

    @ApiOperation(value = "保存奖励清零规则", notes = "保存奖励清零规则")
    @PreAuthorize("hasAuthority('mdc:reward:ruleSave')")
    @ApiMapping(value = "backend.comvita.reward.clean.save", method = RequestMethod.POST, permission = true)
    public void saveRewardClean(RewardCleanSaveParam param) {
        memberRewardService.saveRewardClean(param);
    }

    @ApiOperation(value = "获取奖励清零规则", notes = "获取奖励清零规则")
    @PreAuthorize("hasAuthority('mdc:reward:ruleGet')")
    @ApiMapping(value = "backend.comvita.reward.clean.get", method = RequestMethod.POST, permission = true)
    public CleanRewardRuleResult getRewardClean(RewardRuleGetParam param) {
        return memberRewardService.getRewardClean(param);
    }

    @ApiOperation(value = "启用/停用规则", notes = "编辑规则状态,启用/停用")
    @PreAuthorize("hasAuthority('mdc:reward:ruleSave')")
    @ApiMapping(value = "backend.comvita.reward.ruleState.toggle", method = RequestMethod.POST, permission = true)
    public void toggleState(RewardRuleIdGetParam param) {
        memberRewardService.toggleState(param);
    }

    @ApiOperation(value = "查询奖励记录列表", notes = "查询奖励记录列表")
    @PreAuthorize("hasAuthority('mdc:reward:list')")
    @ApiMapping(value = "backend.comvita.reward.record.page", method = RequestMethod.POST, permission = true)
    public IPage<MemberRewardRecord> page(RewardRecordPageParam param) {
        return memberRewardService.page(param);
    }

    @ApiOperation(value = "新增会员奖励记录", notes = "新增会员奖励记录,增加奖励或扣减奖励")
    @PreAuthorize("hasAuthority('mdc:reward:save')")
    @ApiMapping(value = "backend.comvita.reward.record.add", method = RequestMethod.POST, permission = true)
    public void addRecord(RewardRecordAddParam param) {
        memberRewardService.addRecord(param);
    }

    @ApiOperation(value = "查询奖励排名", notes = "查询奖励排名")
    @PreAuthorize("hasAuthority('mdc:reward:list')")
    @ApiMapping(value = "backend.comvita.reward.recordRank.list", method = RequestMethod.POST, permission = true)
    public List<RewardRankResult> listRank(RewardRankListParam param) {
        return memberRewardService.listRewardRank(param);
    }

    @ApiOperation(value = "初始化c端奖励排名", notes = "初始化c端奖励排名")
    @PreAuthorize("hasAuthority('mdc:reward:init')")
    @ApiMapping(value = "backend.comvita.reward.recordRank.init", method = RequestMethod.POST, permission = true)
    public void initRank(RewardRankInitParam param) {
        memberRewardService.initRewardRank(param);
    }
}
