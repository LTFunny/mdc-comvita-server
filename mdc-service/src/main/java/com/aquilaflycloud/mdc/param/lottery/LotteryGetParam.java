package com.aquilaflycloud.mdc.param.lottery;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * LotteryGetParam
 *
 * @author star
 * @date 2020-04-06
 */
@Data
@Accessors(chain = true)
public class LotteryGetParam {
    @ApiModelProperty(value = "抽奖活动id", required = true)
    @NotNull(message = "抽奖活动id不能为空")
    private Long id;
}


