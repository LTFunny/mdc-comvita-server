package com.aquilaflycloud.mdc.result.coupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * RelAnalysisResult
 *
 * @author star
 * @date 2020-03-10
 */
@Data
public class RelAnalysisResult implements Serializable {
    @ApiModelProperty(value = "核销日期")
    private Date verificateDate;

    @ApiModelProperty(value = "核销数量")
    private Long verificateCount;
}
