package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * PrePickingCardUpdateParam
 *
 * @author zengqingjie
 * @date 2020-12-29
 */
@Data
public class PrePickingCardUpdateParam {
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;
}
