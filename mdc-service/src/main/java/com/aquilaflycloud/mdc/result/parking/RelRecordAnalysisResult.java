package com.aquilaflycloud.mdc.result.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * RelRecordAnalysisResult
 *
 * @author star
 * @date 2020-05-11
 */
@Data
public class RelRecordAnalysisResult {
    @ApiModelProperty(value = "记录名称(商户名称)")
    private String recordName;

    @ApiModelProperty(value = "记录总金额")
    private BigDecimal recordTotalAmount;

    @ApiModelProperty(value = "会员数")
    private Integer memberCount;

    @ApiModelProperty(value = "非会员数")
    private Integer nonMemberCount;

    @ApiModelProperty(value = "记录次数")
    private Integer recordCount;

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
