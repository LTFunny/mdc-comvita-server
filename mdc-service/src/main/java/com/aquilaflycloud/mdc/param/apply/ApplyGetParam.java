package com.aquilaflycloud.mdc.param.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * ApplyGetParam
 *
 * @author star
 * @date 2020-02-27
 */
@Data
@Accessors(chain = true)
public class ApplyGetParam {
    @ApiModelProperty(value = "报名活动id", required = true)
    @NotNull(message = "报名活动id不能为空")
    private Long id;
}


