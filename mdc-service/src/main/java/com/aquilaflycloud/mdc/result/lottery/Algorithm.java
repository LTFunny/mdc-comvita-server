package com.aquilaflycloud.mdc.result.lottery;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Algorithm {
    @ApiModelProperty(value = "中奖率(%)")
    private Integer winningOdds;
}