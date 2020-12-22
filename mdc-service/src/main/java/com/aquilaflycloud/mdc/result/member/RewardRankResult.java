package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.model.member.MemberRewardRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * RewardRankResult
 *
 * @author star
 * @date 2019-12-30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RewardRankResult extends MemberRewardRecord implements Serializable {
    @ApiModelProperty(value = "排序")
    private Long rankNo;

    @ApiModelProperty(value = "奖励值")
    private Integer totalReward;

    @ApiModelProperty(value = "等级称号")
    private String gradeTitle;

    private static final long serialVersionUID = 1L;
}
