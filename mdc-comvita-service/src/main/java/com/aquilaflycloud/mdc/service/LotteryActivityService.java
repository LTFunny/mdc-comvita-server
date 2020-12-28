package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.lottery.LotteryActivity;
import com.aquilaflycloud.mdc.model.lottery.LotteryMemberRecord;
import com.aquilaflycloud.mdc.param.lottery.*;
import com.aquilaflycloud.mdc.result.lottery.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * LotteryActivityService
 *
 * @author star
 * @date 2020-04-06
 */
public interface LotteryActivityService {
    IPage<LotteryActivity> pageLottery(LotteryPageParam param);

    LotteryResult getLottery(LotteryGetParam param);

    void addLottery(LotteryAddParam param);

    void editLottery(LotteryEditParam param);

    void toggleState(LotteryGetParam param);

    void releaseLottery(LotteryGetParam param);

    StatisticsResult getStatistics();

    IPage<LotteryMemberRecord> pageRecord(RecordPageParam param);

    IPage<LotteryActivity> pageLotteryActivity(PageParam<LotteryActivity> param);

    LotteryActivityResult getLotteryActivity(LotteryGetParam param);

    List<LotteryMemberRecord> listLotteryRecord(LotteryGetParam param);

    PrizeResult addLotteryRecord(LotteryGetParam param);

    IPage<RecordResult> pageLotteryRecord(LotteryRecordPageParam param);

    IPage<LotteryMemberRecordResult> pageLotteryMemberRecord(PageParam<LotteryMemberRecord> param);
}
