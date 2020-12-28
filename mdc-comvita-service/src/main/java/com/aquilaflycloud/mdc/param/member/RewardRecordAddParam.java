package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class RewardRecordAddParam {
    @ApiModelProperty(value = "会员id", required = true)
    @NotNull(message = "会员id不能为空")
    private Long memberId;

    @ApiModelProperty(value = "奖励类型(member.RewardTypeEnum)", required = true)
    @NotNull(message = "奖励类型不能为空")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "奖励值", required = true)
    @NotNull(message = "奖励值不能为空")
    private Integer rewardValue;

    @ApiModelProperty(value = "奖励描述", required = true)
    @NotNull(message = "奖励描述不能为空")
    private String remark;
}