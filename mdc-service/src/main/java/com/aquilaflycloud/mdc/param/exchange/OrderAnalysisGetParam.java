package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.exchange.PayModeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * OrderAnalysisGetParam
 *
 * @author star
 * @date 2020-06-01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderAnalysisGetParam extends AuthParam implements Serializable {
    @ApiModelProperty(value = "支付方式(exchange.PayModeEnum)")
    private PayModeEnum payMode;

    @ApiModelProperty(value = "创建开始时间", required = true)
    @NotNull(message = "开始时间不能为空")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建结束时间", required = true)
    @NotNull(message = "结束时间不能为空")
    private Date createTimeEnd;
}
