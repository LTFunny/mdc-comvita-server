package com.aquilaflycloud.mdc.param.system;

import com.aquilaflycloud.mdc.enums.system.ExportTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * ExcelDownloadParam
 *
 * @author star
 * @date 2019-12-09
 */
@Data
public class ExcelDownloadParam {

    @ApiModelProperty(value = "导出类型(system.ExportTypeEnum)", required = true)
    @NotNull(message = "导出类型不能为空")
    private ExportTypeEnum exportType;

    @ApiModelProperty(value = "导出条件")
    private String exportParam;

    @ApiModelProperty(value = "导出文件名称(导出文件名会自动加上当前日期时间yyyyMMddHHmmss)")
    private String exportName;

    @ApiModelProperty(value = "导出字段(为空时导出所有字段)")
    private List<String> exportFields;

    @ApiModelProperty(value = "导出字段别名(为空时为实体类属性名)")
    private List<AliasName> exportAliasNames;

    @Data
    public class AliasName {
        @ApiModelProperty(value = "导出字段")
        private String field;

        @ApiModelProperty(value = "导出字段别名")
        private String alias;
    }
}
