package com.aquilaflycloud.mdc.param.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderInfoGetParam {
    @ApiModelProperty(value = "订单id", required = true)
    private Long id;
}
