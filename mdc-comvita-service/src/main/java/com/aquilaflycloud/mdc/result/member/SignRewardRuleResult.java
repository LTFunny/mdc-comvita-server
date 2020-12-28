package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * SignRewardRuleResult
 *
 * @author star
 * @date 2019-12-17
 */
@Data
public class SignRewardRuleResult {
    @ApiModelProperty(value = "奖励类型")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "每次签到奖励")
    private Integer signReward;

    @ApiModelProperty(value = "连续签到次数")
    private Integer times;

    @ApiModelProperty(value = "满足连续签到次数,额外奖励")
    private Integer extReward;
}
