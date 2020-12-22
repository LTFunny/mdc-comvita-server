package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class MemberRewardRankGetParam {
    @ApiModelProperty(value = "奖励类型(member.RewardTypeEnum)", required = true)
    @NotNull(message = "奖励类型不能为空")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "排名类型(ALL, MONTH, YEAR)", required = true)
    @NotNull(message = "排名类型不能为空")
    private RankTypeEnum rankType;

    @ApiModelProperty(value = "返回排名数,最大1000,默认20")
    @Max(value = 1000, message = "不能超过1000")
    private Integer limit = 20;

    public enum RankTypeEnum {
        // 排名类型
        ALL, MONTH, YEAR
    }
}