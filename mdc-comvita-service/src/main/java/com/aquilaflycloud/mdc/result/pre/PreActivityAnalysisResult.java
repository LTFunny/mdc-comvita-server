package com.aquilaflycloud.mdc.result.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PreActivityAnalysisResult
 * 活动概况返回结果集
 * @author linkq
 */
@Data
public class PreActivityAnalysisResult {

    /**
     * 参与人数
     */
    @ApiModelProperty(value = "参与人数")
    private Long participantsCount;

    /**
     * 转化金额
     */
    @ApiModelProperty(value = "转化金额")
    private BigDecimal exchangePrice;

    /**
     * 客单价
     */
    @ApiModelProperty(value = "客单价")
    private BigDecimal pricePerCustomer;

}
