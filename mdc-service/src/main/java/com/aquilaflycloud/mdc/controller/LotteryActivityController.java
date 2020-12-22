package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.lottery.LotteryActivity;
import com.aquilaflycloud.mdc.model.lottery.LotteryMemberRecord;
import com.aquilaflycloud.mdc.param.lottery.*;
import com.aquilaflycloud.mdc.result.lottery.LotteryResult;
import com.aquilaflycloud.mdc.result.lottery.StatisticsResult;
import com.aquilaflycloud.mdc.service.LotteryActivityService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * LotteryActivityController
 *
 * @author star
 * @date 2020-04-06
 */
@RestController
@Api(tags = "抽奖活动管理")
public class LotteryActivityController {
    @Resource
    private LotteryActivityService lotteryActivityService;

    @ApiOperation(value = "获取抽奖活动列表(分页)", notes = "获取抽奖活动列表(分页)")
    @PreAuthorize("hasAuthority('mdc:lottery:list')")
    @ApiMapping(value = "backend.mdc.lottery.info.page", method = RequestMethod.POST, permission = true)
    public IPage<LotteryActivity> pageLottery(LotteryPageParam param) {
        return lotteryActivityService.pageLottery(param);
    }

    @ApiOperation(value = "获取抽奖活动详情", notes = "获取抽奖活动详情")
    @PreAuthorize("hasAuthority('mdc:lottery:get')")
    @ApiMapping(value = "backend.mdc.lottery.info.get", method = RequestMethod.POST, permission = true)
    public LotteryResult getLottery(LotteryGetParam param) {
        return lotteryActivityService.getLottery(param);
    }

    @ApiOperation(value = "新增抽奖活动", notes = "新增抽奖活动")
    @PreAuthorize("hasAuthority('mdc:lottery:add')")
    @ApiMapping(value = "backend.mdc.lottery.info.add", method = RequestMethod.POST, permission = true)
    public void addLottery(LotteryAddParam param) {
        lotteryActivityService.addLottery(param);
    }

    @ApiOperation(value = "编辑抽奖活动", notes = "编辑抽奖活动")
    @PreAuthorize("hasAuthority('mdc:lottery:edit')")
    @ApiMapping(value = "backend.mdc.lottery.info.edit", method = RequestMethod.POST, permission = true)
    public void editLottery(LotteryEditParam param) {
        lotteryActivityService.editLottery(param);
    }

    @ApiOperation(value = "启用/停用抽奖活动", notes = "编辑抽奖活动状态,启用/停用")
    @PreAuthorize("hasAuthority('mdc:lottery:edit')")
    @ApiMapping(value = "backend.mdc.lottery.state.toggle", method = RequestMethod.POST, permission = true)
    public void toggleLotteryState(LotteryGetParam param) {
        lotteryActivityService.toggleState(param);
    }

    @ApiOperation(value = "发布抽奖活动", notes = "发布抽奖活动")
    @PreAuthorize("hasAuthority('mdc:lottery:edit')")
    @ApiMapping(value = "backend.mdc.lottery.info.release", method = RequestMethod.POST, permission = true)
    public void releaseLottery(LotteryGetParam param) {
        lotteryActivityService.releaseLottery(param);
    }

    @ApiOperation(value = "获取抽奖活动概况统计", notes = "获取抽奖活动概况统计")
    @PreAuthorize("hasAuthority('mdc:lottery:list')")
    @ApiMapping(value = "backend.mdc.lottery.statistics.get", method = RequestMethod.POST, permission = true)
    public StatisticsResult getStatistics() {
        return lotteryActivityService.getStatistics();
    }

    @ApiOperation(value = "获取抽奖记录列表(分页)", notes = "获取抽奖记录列表(分页)")
    @PreAuthorize("hasAuthority('mdc:lotteryRecord:list')")
    @ApiMapping(value = "backend.mdc.lottery.record.page", method = RequestMethod.POST, permission = true)
    public IPage<LotteryMemberRecord> pageRecord(RecordPageParam param) {
        return lotteryActivityService.pageRecord(param);
    }
}
