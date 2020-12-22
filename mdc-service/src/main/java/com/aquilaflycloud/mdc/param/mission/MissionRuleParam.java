package com.aquilaflycloud.mdc.param.mission;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;

/**
 * MissionRuleParam
 *
 * @author star
 * @date 2020-05-08
 */
@AnotherFieldHasValue(fieldName = "rewardValue", notNullFieldName = "rewardType", message = "奖励类型不能为空")
@Data
@Accessors(chain = true)
public class MissionRuleParam {
    @ApiModelProperty(value = "完成条件")
    private Integer condition = 1;

    @ApiModelProperty(value = "奖励类型(member.RewardTypeEnum)")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "奖励值")
    @Min(value = 1, message = "奖励值不能小于1")
    private Integer rewardValue;
}


