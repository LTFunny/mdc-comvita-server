package com.aquilaflycloud.mdc.result.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * RelDetailAnalysisResult
 *
 * @author star
 * @date 2020-01-22
 */
@Data
public class RelDetailAnalysisResult {
    @ApiModelProperty(value = "停车券名称")
    private String couponName;

    @ApiModelProperty(value = "商户名称")
    private String designateOrgNames;

    @ApiModelProperty(value = "派发金额")
    private BigDecimal distributeAmount;

    @ApiModelProperty(value = "派发时长")
    private BigDecimal distributeTime;

    @ApiModelProperty(value = "派发数量")
    private Integer distributeCount;

    @ApiModelProperty(value = "核销金额")
    private BigDecimal consumeAmount;

    @ApiModelProperty(value = "核销时长")
    private BigDecimal consumeTime;

    @ApiModelProperty(value = "核销数量")
    private Integer consumeCount;
}
