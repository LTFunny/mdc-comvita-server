package com.aquilaflycloud.mdc.param.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * SqlExecuteParam
 *
 * @author star
 * @date 2020/8/31
 */
@Data
@Accessors(chain = true)
public class SqlExecuteParam {
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "sql动态参数值")
    @Valid
    private List<SqlExecuteWhereParam> paramValues = new ArrayList<>();
}


