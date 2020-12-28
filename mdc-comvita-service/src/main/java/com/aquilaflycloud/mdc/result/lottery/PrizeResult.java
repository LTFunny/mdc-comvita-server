package com.aquilaflycloud.mdc.result.lottery;

import com.aquilaflycloud.mdc.model.lottery.LotteryPrize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PrizeResult
 *
 * @author star
 * @date 2020-04-07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PrizeResult extends LotteryPrize {
    @ApiModelProperty(value = "抽奖算法")
    private Algorithm algorithm;

    @ApiModelProperty(value = "奖励内容")
    private LotteryReward lotteryReward;

    @ApiModelProperty(value = "优惠券")
    private LotteryCoupon lotteryCoupon;

}
