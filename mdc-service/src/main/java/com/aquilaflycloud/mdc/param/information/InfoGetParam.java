package com.aquilaflycloud.mdc.param.information;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * InfoGetParam
 *
 * @author star
 * @date 2020-03-07
 */
@Data
@Accessors(chain = true)
public class InfoGetParam {
    @ApiModelProperty(value = "资讯id", required = true)
    @NotNull(message = "资讯id不能为空")
    private Long id;
}


