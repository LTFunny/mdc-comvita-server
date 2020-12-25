package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.lottery.LotteryActivity;
import com.aquilaflycloud.mdc.model.lottery.LotteryMemberRecord;
import com.aquilaflycloud.mdc.param.lottery.LotteryGetParam;
import com.aquilaflycloud.mdc.param.lottery.LotteryRecordPageParam;
import com.aquilaflycloud.mdc.result.lottery.LotteryActivityResult;
import com.aquilaflycloud.mdc.result.lottery.LotteryMemberRecordResult;
import com.aquilaflycloud.mdc.result.lottery.PrizeResult;
import com.aquilaflycloud.mdc.result.lottery.RecordResult;
import com.aquilaflycloud.mdc.service.LotteryActivityService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * LotteryActivityApi
 *
 * @author star
 * @date 2020-04-07
 */
@RestController
@Api(tags = "抽奖活动接口")
public class LotteryActivityApi {

    @Resource
    private LotteryActivityService lotteryActivityService;

    @ApiOperation(value = "获取抽奖活动列表", notes = "获取抽奖活动列表")
    @ApiMapping(value = "comvita.lottery.info.page", method = RequestMethod.POST)
    public IPage<LotteryActivity> pageLotteryActivity(PageParam<LotteryActivity> param) {
        return lotteryActivityService.pageLotteryActivity(param);
    }

    @ApiOperation(value = "获取抽奖活动详情", notes = "获取抽奖活动详情")
    @ApiMapping(value = "comvita.lottery.info.get", method = RequestMethod.POST)
    public LotteryActivityResult getLotteryActivity(LotteryGetParam param) {
        return lotteryActivityService.getLotteryActivity(param);
    }

    @ApiOperation(value = "抽奖记录列表", notes = "抽奖记录列表")
    @ApiMapping(value = "comvita.lottery.record.list", method = RequestMethod.POST)
    public List<LotteryMemberRecord> listLotteryRecord(LotteryGetParam param) {
        return lotteryActivityService.listLotteryRecord(param);
    }

    @ApiOperation(value = "新增抽奖记录", notes = "新增抽奖记录,会员抽奖并返回中奖奖品")
    @ApiMapping(value = "comvita.lottery.record.add", method = RequestMethod.POST)
    public PrizeResult addLotteryRecord(LotteryGetParam param) {
        return lotteryActivityService.addLotteryRecord(param);
    }

    @ApiOperation(value = "获取抽奖记录列表", notes = "获取抽奖记录列表")
    @ApiMapping(value = "comvita.lottery.record.page", method = RequestMethod.POST)
    public IPage<RecordResult> pageLotteryRecord(LotteryRecordPageParam param) {
        return lotteryActivityService.pageLotteryRecord(param);
    }

    @ApiOperation(value = "获取抽奖活动记录列表", notes = "获取抽奖活动记录列表")
    @ApiMapping(value = "comvita.lottery.memberRecord.page", method = RequestMethod.POST)
    public IPage<LotteryMemberRecordResult> pageLotteryMemberRecord(PageParam<LotteryMemberRecord> param) {
        return lotteryActivityService.pageLotteryMemberRecord(param);
    }
}
