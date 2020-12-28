package com.aquilaflycloud.mdc.result.mission;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * MissionRuleResult
 *
 * @author star
 * @date 2020-05-08
 */
@Data
@Accessors(chain = true)
public class MissionRuleResult {
    @ApiModelProperty(value = "完成条件")
    private Integer condition;

    @ApiModelProperty(value = "奖励类型(member.RewardTypeEnum)")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "奖励值")
    private Integer rewardValue = 0;
}


