package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class MemberGradeAddParam {
    @ApiModelProperty(value = "微信或支付宝appId", required = true)
    @NotBlank(message = "appId不能为空")
    private String appId;

    @ApiModelProperty(value = "奖励类型(member.RewardTypeEnum)", required = true)
    @NotNull(message = "奖励类型不能为空")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "等级称号", required = true)
    @NotBlank(message = "等级称号不能为空")
    private String gradeTitle;

    @ApiModelProperty(value = "奖励范围最小值", required = true)
    @NotNull(message = "奖励范围最小值不能为空")
    private Integer minValue;

    @ApiModelProperty(value = "奖励范围最大值", required = true)
    @NotNull(message = "奖励范围最大值不能为空")
    private Integer maxValue;

    @ApiModelProperty(value = "等级排序", required = true)
    @NotNull(message = "等级排序不能为空")
    private Integer gradeOrder;
}
