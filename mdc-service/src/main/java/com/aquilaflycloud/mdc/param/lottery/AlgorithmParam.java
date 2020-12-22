package com.aquilaflycloud.mdc.param.lottery;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class AlgorithmParam {
    @ApiModelProperty(value = "中奖率(%)")
    @Min(value = 0, message = "中奖率不能小于0")
    private Integer winningOdds;
}