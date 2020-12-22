package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * MemberRewardResult
 *
 * @author star
 * @date 2020-01-03
 */
@Data
@Accessors(chain = true)
public class MemberRewardResult implements Serializable {
    @ApiModelProperty(value = "奖励类型")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "奖励值")
    private Integer rewardValue;
}
