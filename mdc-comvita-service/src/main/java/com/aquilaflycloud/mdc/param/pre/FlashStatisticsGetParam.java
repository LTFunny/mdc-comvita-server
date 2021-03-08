package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * FlashStatisticsGetParam
 *
 * @author star
 * @date 2021/3/8
 */
@Data
public class FlashStatisticsGetParam {
    @ApiModelProperty(value = "活动id", required = true)
    @NotNull(message = "活动id不能为空")
    private Long id;
}
