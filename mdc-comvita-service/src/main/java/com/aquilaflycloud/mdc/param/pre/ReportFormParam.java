package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 报表查询参数
 * <p>
 * zly
 */
@Accessors(chain = true)
@Data
public class ReportFormParam extends PageParam {

    @ApiModelProperty(value = "导购员名称")
    private String guideName;

    @ApiModelProperty(value = "门店id")
    private String shopId;

    @ApiModelProperty(value = "统计开始时间")
    private Date createStartTime;

    @ApiModelProperty(value = "统计结束时间")
    private Date createEndTime;
}
