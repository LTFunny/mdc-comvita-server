package com.aquilaflycloud.mdc.result.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * RelStatisticsResult
 *
 * @author star
 * @date 2020-01-21
 */
@Data
public class RelStatisticsResult {
    @ApiModelProperty(value = "派发数")
    private Long distributeCount;

    @ApiModelProperty(value = "派发金额")
    private BigDecimal distributeAmount;

    @ApiModelProperty(value = "派发时长")
    private BigDecimal distributeTime;

    @ApiModelProperty(value = "核销数")
    private Long consumeCount;

    @ApiModelProperty(value = "核销金额")
    private BigDecimal consumeAmount;

    @ApiModelProperty(value = "核销时长")
    private BigDecimal consumeTime;

    @ApiModelProperty(value = "车辆数")
    private Long carCount;

    @ApiModelProperty(value = "会员数")
    private Long memberCount;
}
