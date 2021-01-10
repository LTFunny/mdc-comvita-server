package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PreRuleOrderFullReduceParam
 * 规则 下单满减参数
 * @author linkq
 */
@Data
public class PreRuleOrderFullReduceParam {

    /**
     * 商品id
     */
    @ApiModelProperty(value = "满多少钱")
    private BigDecimal fullPrice;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "减多少钱")
    private BigDecimal reducePrice;

}
