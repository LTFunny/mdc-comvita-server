package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PreActivityRewardResult
 *
 * @author star
 * @date 2021/1/7
 */
@Data
public class PreActivityRewardResult {
    @ApiModelProperty(value = "奖励类型")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "奖励值")
    private Integer rewardValue;
}
