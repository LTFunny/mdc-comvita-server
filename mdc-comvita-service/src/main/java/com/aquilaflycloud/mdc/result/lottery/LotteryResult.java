package com.aquilaflycloud.mdc.result.lottery;

import com.aquilaflycloud.mdc.model.lottery.LotteryActivity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * LotteryResult
 *
 * @author star
 * @date 2020-04-06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LotteryResult extends LotteryActivity {
    @ApiModelProperty(value = "抽奖规则")
    private LotteryRule lotteryRule;

    @ApiModelProperty(value = "抽奖算法")
    private Algorithm algorithm;

    @ApiModelProperty(value = "抽奖总次数")
    private Long lotteryNum;

    @ApiModelProperty(value = "中奖次数")
    private Long wonNum;

    @ApiModelProperty(value = "奖品总数")
    private Long prizeNum;

    @ApiModelProperty(value = "奖品列表")
    private List<PrizeResult> prizeList;
}
