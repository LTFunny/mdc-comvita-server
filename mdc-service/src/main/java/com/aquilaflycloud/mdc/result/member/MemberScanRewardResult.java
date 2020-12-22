package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * MemberScanRewardResult
 *
 * @author star
 * @date 2020-03-30
 */
@Data
@Accessors(chain = true)
public class MemberScanRewardResult implements Serializable {
    @ApiModelProperty(value = "奖励类型")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "是否超过限制")
    private Boolean overLimit;

    @ApiModelProperty(value = "奖励值")
    private Integer rewardValue;

    @ApiModelProperty(value = "每次最大奖励值")
    private Integer preMaxReward;

    @ApiModelProperty(value = "每天最大奖励值")
    private Integer dayMaxReward;

    @ApiModelProperty(value = "当天已领奖励值")
    private Integer alreadyDayReward;

    @ApiModelProperty(value = "可领奖励值")
    private Integer canReward;
}
