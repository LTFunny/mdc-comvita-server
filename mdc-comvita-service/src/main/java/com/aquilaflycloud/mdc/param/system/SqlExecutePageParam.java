package com.aquilaflycloud.mdc.param.system;

import com.aquilaflycloud.dataAuth.common.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * SqlExecutePageParam
 *
 * @author star
 * @date 2020/8/31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SqlExecutePageParam extends PageParam {
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "sql动态参数值")
    @Valid
    private List<SqlExecuteWhereParam> paramValues;
}


