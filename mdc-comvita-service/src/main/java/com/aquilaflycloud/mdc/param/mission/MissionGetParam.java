package com.aquilaflycloud.mdc.param.mission;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * MissionGetParam
 *
 * @author star
 * @date 2020-05-08
 */
@Data
@Accessors(chain = true)
public class MissionGetParam {
    @ApiModelProperty(value = "任务id", required = true)
    @NotNull(message = "任务id不能为空")
    private Long id;
}


