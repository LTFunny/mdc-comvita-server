package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.mdc.enums.pre.RuleStateEnum;
import com.aquilaflycloud.mdc.enums.pre.RuleTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    @ApiModelProperty(value = "规则类型(3-下单满减、2-下单折扣、1-下单即送)")
    private RuleTypeEnum ruleType;

    /**
     * 类型详情  保存为json串 样例如: type为下单满减 满100减10  {"full_price":"100","reduce_price":"10"}
     */
    @ApiModelProperty(value = "类型详情")
    private String typeDetail;

    /**
     * 状态(1-已启用、0-已停用)
     */
    @ApiModelProperty(value = "状态(1-已启用、0-已停用)")
    private RuleStateEnum ruleState;

    /**
     * 是否默认(0-否 1-是)
     */
    @ApiModelProperty(value = "是否默认(0-否 1-是)")
    private Integer isDefault;


}
