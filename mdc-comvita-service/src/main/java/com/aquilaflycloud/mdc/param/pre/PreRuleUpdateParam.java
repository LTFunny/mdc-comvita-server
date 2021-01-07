package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.mdc.enums.pre.RuleDefaultEnum;
import com.aquilaflycloud.mdc.enums.pre.RuleStateEnum;
import com.aquilaflycloud.mdc.enums.pre.RuleTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * PreRuleUpdateParam
 * @author linkq
 */
@Data
public class PreRuleUpdateParam {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    /**
     * 规则类型(3-下单满减、2-下单折扣、1-下单即送)
     */
    @ApiModelProperty(value = "规则类型(pre.RuleTypeEnum)")
    private RuleTypeEnum ruleType;

    /**
     * 类型详情  保存为json串 样例如: type为下单满减 满100减10  {"full_price":"100","reduce_price":"10"}
     */
    @ApiModelProperty(value = "类型详情")
    private String typeDetail;

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
