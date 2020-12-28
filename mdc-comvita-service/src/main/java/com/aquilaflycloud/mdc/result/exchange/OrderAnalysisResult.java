package com.aquilaflycloud.mdc.result.exchange;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * OrderAnalysisResult
 *
 * @author star
 * @date 2020-06-01
 */
@Data
public class OrderAnalysisResult {
    @ApiModelProperty(value = "成交订单数")
    private Integer dealCount;

    @ApiModelProperty(value = "成交金额")
    private BigDecimal dealPrice;

    @ApiModelProperty(value = "创建日期")
    private Date createDate;
}
