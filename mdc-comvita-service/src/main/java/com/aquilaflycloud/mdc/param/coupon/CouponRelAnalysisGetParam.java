package com.aquilaflycloud.mdc.param.coupon;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * CouponRelAnalysisGetParam
 *
 * @author star
 * @date 2020-03-10
 */
@Data
public class CouponRelAnalysisGetParam implements Serializable {
    @ApiModelProperty(value = "优惠券id")
    private Long id;

    @ApiModelProperty(value = "核销开始日期", required = true)
    @NotNull(message = "核销开始日期不能为空")
    private Date verificateDateStart;

    @ApiModelProperty(value = "核销结束日期", required = true)
    @NotNull(message = "核销结束日期不能为空")
    private Date verificateDateEnd;
}
