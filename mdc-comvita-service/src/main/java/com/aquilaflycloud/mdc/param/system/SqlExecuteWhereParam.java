package com.aquilaflycloud.mdc.param.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * SqlExecuteWhereParam
 *
 * @author star
 * @date 2020/9/1
 */
@Data
@Accessors(chain = true)
public class SqlExecuteWhereParam {
    @ApiModelProperty(value = "参数名称", required = true)
    @NotBlank(message = "参数名称不能为空")
    private String name;

    @ApiModelProperty(value = "参数值", required = true)
    @NotBlank(message = "参数值不能为空")
    private String value;
}


