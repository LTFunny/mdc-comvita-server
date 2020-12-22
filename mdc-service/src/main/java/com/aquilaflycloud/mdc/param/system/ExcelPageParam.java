package com.aquilaflycloud.mdc.param.system;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.system.ExportStateEnum;
import com.aquilaflycloud.mdc.enums.system.ExportTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * ExcelPageParam
 *
 * @author star
 * @date 2019-12-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExcelPageParam extends PageAuthParam {
    @ApiModelProperty(value = "导出类型(system.ExportTypeEnum)")
    private ExportTypeEnum exportType;

    @ApiModelProperty(value = "任务状态(system.ExportStateEnum)")
    private ExportStateEnum state;

    @ApiModelProperty(value = "导出开始时间")
    private Date startExportTime;

    @ApiModelProperty(value = "导出结束时间")
    private Date endExportTime;
}
