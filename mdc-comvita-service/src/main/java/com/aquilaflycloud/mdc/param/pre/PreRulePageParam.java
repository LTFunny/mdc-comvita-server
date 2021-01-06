package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.RuleStateEnum;
import com.aquilaflycloud.mdc.enums.pre.RuleTypeEnum;
import com.aquilaflycloud.mdc.model.pre.PreRuleInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * PreRulePageParam
 *
 * @author linkq
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PreRulePageParam extends PageParam<PreRuleInfo> {

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    /**
     * 规则类型(3-下单满减、2-下单折扣、1-下单即送)
     */
    @ApiModelProperty(value = "规则类型(pre.RuleTypeEnum)")
    private RuleTypeEnum ruleType;

    /**
     * 状态(1-已启用、0-已停用)
     */
    @ApiModelProperty(value = "状态(pre.RuleStateEnum)")
    private RuleStateEnum ruleState;

}
