package com.aquilaflycloud.mdc.result.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * RelSummaryAnalysisResult
 *
 * @author star
 * @date 2020-01-22
 */
@Data
public class RelSummaryAnalysisResult {
    @ApiModelProperty(value = "统计日期")
    private String analysisDate;

    @ApiModelProperty(value = "优惠金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "优惠时长")
    private BigDecimal totalTime;

    @ApiModelProperty(value = "最后优惠时间")
    private Date lastTime;
}
