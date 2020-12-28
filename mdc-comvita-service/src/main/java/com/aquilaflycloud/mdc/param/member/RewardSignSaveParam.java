package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "times", notNullFieldName = "extReward", message = "额外奖励不能为空"),
        @AnotherFieldHasValue(fieldName = "extReward", notNullFieldName = "times", message = "连续签到次数不能为空")
})
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class RewardSignSaveParam extends RewardSaveParam {
    @ApiModelProperty(value = "每次签到奖励", required = true)
    @Min(value = 0, message = "奖励不能小于0")
    @NotNull(message = "奖励不能为空")
    private Integer signReward;

    @ApiModelProperty(value = "连续签到次数")
    @Min(value = 1, message = "连续签到次数不能小于1")
    private Integer times;

    @ApiModelProperty(value = "满足连续签到次数,额外奖励")
    @Min(value = 0, message = "额外奖励不能小于0")
    private Integer extReward;
}