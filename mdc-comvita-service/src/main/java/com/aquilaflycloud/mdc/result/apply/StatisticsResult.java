package com.aquilaflycloud.mdc.result.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * StatisticsResult
 *
 * @author star
 * @date 2020-02-27
 */
@Data
public class StatisticsResult {
    @ApiModelProperty(value = "活动总数")
    private Long total;

    @ApiModelProperty(value = "有效活动总数")
    private Long effectiveTotal;

    @ApiModelProperty(value = "累计报名人数")
    private Long applyTotal;

    @ApiModelProperty(value = "累计报名成功人数")
    private Long applySuccessTotal;
}
