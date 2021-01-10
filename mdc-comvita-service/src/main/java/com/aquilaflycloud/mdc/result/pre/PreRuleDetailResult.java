package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreRuleInfo;
import com.aquilaflycloud.mdc.param.pre.PreRuleGoodsParam;
import com.aquilaflycloud.mdc.param.pre.PreRuleOrderFullReduceParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * PreActivityDetailResult
 * @author linkq
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PreRuleDetailResult extends PreRuleInfo {

    @ApiModelProperty(value = "下单满减参数")
    private PreRuleOrderFullReduceParam orderFullReduce;

    @ApiModelProperty(value = "下单折扣参数")
    private BigDecimal discount;

    @ApiModelProperty(value = "下单即送参数商品关联信息")
    private List<PreRuleGoodsParam> refGoods;


}
