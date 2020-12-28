package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import com.aquilaflycloud.mdc.model.member.MemberRewardRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MemberRewardPageParam extends PageParam<MemberRewardRecord> {
    @ApiModelProperty(value = "奖励类型(member.RewardTypeEnum)", required = true)
    @NotNull(message = "奖励类型不能为空")
    private RewardTypeEnum rewardType;
}