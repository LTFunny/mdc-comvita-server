package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.enums.common.DateUnitTypeEnum;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * CleanRewardRuleResult
 *
 * @author star
 * @date 2020-03-06
 */
@Data
public class CleanRewardRuleResult {
    @ApiModelProperty(value = "奖励类型")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "清零日期(MM-dd)")
    private String cleanDate;

    @ApiModelProperty(value = "延时清零时间单位")
    private DateUnitTypeEnum dateUnitType;

    @ApiModelProperty(value = "延时清零时间")
    private Integer cleanDelay;
}
