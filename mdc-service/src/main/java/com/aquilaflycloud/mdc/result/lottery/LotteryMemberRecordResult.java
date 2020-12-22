package com.aquilaflycloud.mdc.result.lottery;

import com.aquilaflycloud.mdc.model.lottery.LotteryActivity;
import com.aquilaflycloud.mdc.model.lottery.LotteryMemberRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * LotteryMemberRecordResult
 *
 * @author star
 * @date 2020-05-08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LotteryMemberRecordResult extends LotteryMemberRecord {
    @ApiModelProperty(value = "抽奖活动信息")
    private LotteryActivity lotteryActivity;
}
