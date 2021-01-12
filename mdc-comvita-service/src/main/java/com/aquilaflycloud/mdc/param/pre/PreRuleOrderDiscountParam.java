package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PreRuleOrderDiscountParam
 * 规则 下单折扣参数
 * @author linkq
 */
@Data
public class PreRuleOrderDiscountParam {

    @ApiModelProperty(value = "下单折扣参数")
    private BigDecimal discount;

}
