package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class RewardRuleIdGetParam {
    @ApiModelProperty(value = "规则id", required = true)
    @NotNull(message = "规则id不能为空")
    private Long id;
}