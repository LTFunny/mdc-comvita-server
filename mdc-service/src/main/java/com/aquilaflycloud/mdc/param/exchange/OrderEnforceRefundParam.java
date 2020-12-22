package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.common.NoneBlank;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * OrderEnforceRefundParam
 *
 * @author star
 * @date 2020-05-20
 */
@NoneBlank(fieldNames = {"id", "orderCode"}, message = "订单id和订单编号不能同时为空")
@Data
public class OrderEnforceRefundParam implements Serializable {
    @ApiModelProperty(value = "订单id")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderCode;
}
