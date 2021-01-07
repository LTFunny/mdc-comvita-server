package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * PreActivityRewardParam
 *
 * @author star
 * @date 2021/1/7
 */
@Data
public class PreActivityRewardParam {
    @ApiModelProperty(value = "奖励类型(member.RewardTypeEnum)", required = true)
    @NotNull(message = "奖励类型不能为空")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "奖励值", required = true)
    @NotNull(message = "奖励值不能为空")
    private Integer rewardValue;
}
