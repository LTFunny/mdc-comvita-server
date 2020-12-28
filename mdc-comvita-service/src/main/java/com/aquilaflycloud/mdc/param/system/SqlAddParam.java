package com.aquilaflycloud.mdc.param.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * SqlAddParam
 *
 * @author star
 * @date 2020/8/31
 */
@Data
@Accessors(chain = true)
public class SqlAddParam {
    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "sql内容", required = true)
    @NotBlank(message = "sql内容不能为空")
    private String sqlContent;

    @ApiModelProperty(value = "字段信息", required = true)
    @NotEmpty(message = "字段信息不能为空")
    @Valid
    private List<SqlColumnParam> columnList;

    @ApiModelProperty(value = "sql的where内容")
    private String sqlWhereContent;

    @ApiModelProperty(value = "参数信息")
    @Valid
    private List<SqlWhereParam> paramList;
}


