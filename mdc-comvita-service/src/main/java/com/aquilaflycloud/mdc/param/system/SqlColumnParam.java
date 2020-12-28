package com.aquilaflycloud.mdc.param.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * SqlColumnParam
 *
 * @author star
 * @date 2020/8/31
 */
@Data
@Accessors(chain = true)
public class SqlColumnParam {
    @ApiModelProperty(value = "字段名称", required = true)
    @NotBlank(message = "字段名称不能为空")
    private String name;

    @ApiModelProperty(value = "字段别名")
    private String alias;
}


