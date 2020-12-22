package com.aquilaflycloud.mdc.result.lottery;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LotteryReward {
    @ApiModelProperty(value = "奖励类型")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "奖励值")
    private Integer rewardValue;
}