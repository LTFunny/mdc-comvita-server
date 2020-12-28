package com.aquilaflycloud.mdc.param.system;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.system.SqlParamTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * SqlWhereParam
 *
 * @author star
 * @date 2020/9/1
 */
@AnotherFieldHasValue(fieldName = "type", fieldValue = "ARRAY", notNullFieldName = "preset", message = "数组类型参数预设值不能为空")
@Data
@Accessors(chain = true)
public class SqlWhereParam {
    @ApiModelProperty(value = "参数名称", required = true)
    @NotBlank(message = "参数名称不能为空")
    private String name;

    @ApiModelProperty(value = "参数别名")
    private String alias;

    @ApiModelProperty(value = "参数类型(system.SqlParamTypeEnum)", required = true)
    @NotNull(message = "参数类型不能为空")
    private SqlParamTypeEnum type;

    @ApiModelProperty(value = "预设值(多个以,分割)")
    private String preset;
}


