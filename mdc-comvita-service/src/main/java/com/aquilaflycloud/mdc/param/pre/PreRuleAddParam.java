package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.mdc.enums.pre.RuleDefaultEnum;
import com.aquilaflycloud.mdc.enums.pre.RuleStateEnum;
import com.aquilaflycloud.mdc.enums.pre.RuleTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * PreRuleAddParam
 * @author linkq
 */
@Data
public class PreRuleAddParam {

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    /**
     * 规则类型(3-下单满减、2-下单折扣、1-下单即送)
     */
    @ApiModelProperty(value = "规则类型(pre.RuleTypeEnum)")
    private RuleTypeEnum ruleType;

    @ApiModelProperty(value = "下单满减参数")
    private PreRuleOrderFullReduceParam orderFullReduce;

    @ApiModelProperty(value = "下单折扣参数")
    private BigDecimal discount;

    @ApiModelProperty(value = "下单即送参数商品关联信息")
    private List<PreRuleGoodsParam> refGoods;

    /**
     * 状态(1-已启用、0-已停用)
     */
    @ApiModelProperty(value = "状态(pre.RuleStateEnum)")
    private RuleStateEnum ruleState;

    /**
     * 是否默认(0-否 1-是)
     */
    @ApiModelProperty(value = "是否默认(pre.RuleDefaultEnum)")
    private RuleDefaultEnum isDefault;


}
