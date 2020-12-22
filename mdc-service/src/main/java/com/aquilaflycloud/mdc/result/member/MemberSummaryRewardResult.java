package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * MemberRewardResult
 *
 * @author star
 * @date 2020-01-03
 */
@Data
@Accessors(chain = true)
public class MemberSummaryRewardResult implements Serializable {
    @ApiModelProperty(value = "奖励类型")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "奖励总值")
    private Integer totalReward;

    @ApiModelProperty(value = "奖励过期日期")
    private Date expireDate;

    @ApiModelProperty(value = "过期奖励值")
    private Integer expireReward;
}
