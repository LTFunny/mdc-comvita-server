package com.aquilaflycloud.mdc.result.lottery;

import com.aquilaflycloud.mdc.model.lottery.LotteryActivity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * LotteryActivityResult
 *
 * @author star
 * @date 2020-04-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LotteryActivityResult extends LotteryActivity {
    @ApiModelProperty(value = "抽奖规则")
    private LotteryRule lotteryRule;

    @ApiModelProperty(value = "剩余抽奖次数(-1表示无限制)")
    private Integer surplusLotteryNum;

    @ApiModelProperty(value = "奖品列表")
    private List<PrizeResult> prizeList;
}
