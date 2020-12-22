package com.aquilaflycloud.mdc.result.system;

import com.aquilaflycloud.mdc.enums.system.SqlParamTypeEnum;
import com.aquilaflycloud.mdc.model.system.SystemSqlInjector;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * SqlResult
 *
 * @author star
 * @date 2020/8/31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SqlResult extends SystemSqlInjector {
    @ApiModelProperty(value = "字段信息列表")
    private List<SqlColumn> columnList;

    @ApiModelProperty(value = "参数信息")
    private List<SqlParam> paramList;

    @Data
    public class SqlColumn {
        @ApiModelProperty(value = "字段名称")
        private String name;

        @ApiModelProperty(value = "字段别名")
        private String alias;
    }

    @Data
    public class SqlParam {
        @ApiModelProperty(value = "参数名称")
        private String name;

        @ApiModelProperty(value = "参数别名")
        private String alias;

        @ApiModelProperty(value = "参数类型")
        private SqlParamTypeEnum type;

        @ApiModelProperty(value = "预设参数值")
        private String preset;
    }
}
