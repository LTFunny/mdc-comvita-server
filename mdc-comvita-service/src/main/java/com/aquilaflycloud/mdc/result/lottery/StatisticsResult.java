package com.aquilaflycloud.mdc.result.lottery;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * StatisticsResult
 *
 * @author star
 * @date 2020-02-27
 */
@Data
public class StatisticsResult {
    @ApiModelProperty(value = "活动总数")
    private Long total;

    @ApiModelProperty(value = "有效活动总数")
    private Long effectiveTotal;

    @ApiModelProperty(value = "累计抽奖数")
    private Long lotteryTotal;

    @ApiModelProperty(value = "累计中奖数")
    private Long wonTotal;
}
