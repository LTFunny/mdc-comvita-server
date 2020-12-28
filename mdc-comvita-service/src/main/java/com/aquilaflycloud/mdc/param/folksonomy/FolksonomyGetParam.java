package com.aquilaflycloud.mdc.param.folksonomy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * FolksonomyGetParam
 *
 * @author star
 * @date 2020-03-07
 */
@Data
@Accessors(chain = true)
public class FolksonomyGetParam {
    @ApiModelProperty(value = "标签id", required = true)
    @NotNull(message = "标签id不能为空")
    private Long id;
}


