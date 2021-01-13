package com.aquilaflycloud.mdc.result.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * PreOrderStatisticsResult
 *
 * @author star
 * @date 2021/1/13
 */
@Accessors(chain = true)
@Data
public class PreOrderStatisticsResult {
    @ApiModelProperty(value = "订单数")
    private String orderCount;

    @ApiModelProperty(value = "订单总价")
    private String orderAllPrice;

    @ApiModelProperty(value = "订单单价")
    private String orderPerPrice;
}
