package com.aquilaflycloud.mdc.result.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ParkingOrderPayResult {
    @ApiModelProperty(value = "订单id")
    private Long id;

    @ApiModelProperty(value = "支付相关参数")
    private String payParam;
}