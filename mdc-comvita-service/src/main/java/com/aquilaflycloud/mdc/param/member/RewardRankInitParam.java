package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
public class RewardRankInitParam {
    @ApiModelProperty(value = "奖励类型(member.RewardTypeEnum)", required = true)
    @NotNull(message = "奖励类型不能为空")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value = "微信或支付宝appId列表")
    private List<String> appIdList;
}
