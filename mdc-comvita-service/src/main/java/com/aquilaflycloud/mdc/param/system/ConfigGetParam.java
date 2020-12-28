package com.aquilaflycloud.mdc.param.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * ConfigGetParam
 *
 * @author star
 * @date 2020-04-09
 */
@Data
public class ConfigGetParam {
    @ApiModelProperty(value="配置id", required = true)
    @NotNull(message = "配置id不能为空")
    private Long id;
}
