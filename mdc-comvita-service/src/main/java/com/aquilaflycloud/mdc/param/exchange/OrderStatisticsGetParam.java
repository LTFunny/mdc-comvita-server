package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.exchange.PayModeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * OrderStatisticsGetParam
 *
 * @author star
 * @date 2020-06-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderStatisticsGetParam extends AuthParam implements Serializable {
    @ApiModelProperty(value = "支付方式(exchange.PayModeEnum)")
    private PayModeEnum payMode;

    @ApiModelProperty(value = "创建开始时间")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建结束时间")
    private Date createTimeEnd;
}
