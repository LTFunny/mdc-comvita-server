package com.aquilaflycloud.mdc.param.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * SqlEditParam
 *
 * @author star
 * @date 2020/8/31
 */
@Data
@Accessors(chain = true)
public class SqlEditParam {
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "sql内容")
    private String sqlContent;

    @ApiModelProperty(value = "字段信息")
    @Valid
    private List<SqlColumnParam> columnList;

    @ApiModelProperty(value = "sql的where内容")
    private String sqlWhereContent;

    @ApiModelProperty(value = "参数信息")
    @Valid
    private List<SqlWhereParam> paramList;
}


