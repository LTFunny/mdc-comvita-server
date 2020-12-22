package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.exchange.PayModeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import java.io.Serializable;
import java.util.Date;

/**
 * GoodsRankGetParam
 *
 * @author star
 * @date 2020-06-02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsRankGetParam extends AuthParam implements Serializable {
    @ApiModelProperty(value = "返回排名数,最大1000,默认20")
    @Max(value = 1000, message = "不能超过1000")
    private Integer limit = 20;

    @ApiModelProperty(value = "支付方式(exchange.PayModeEnum)")
    private PayModeEnum payMode;

    @ApiModelProperty(value = "创建开始时间")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建结束时间")
    private Date createTimeEnd;
}
