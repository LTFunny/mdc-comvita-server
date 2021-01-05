package com.aquilaflycloud.mdc.result.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PreEnableRuleResult
 *
 * @author linkq
 */
@Data
public class PreEnableRuleResult {
    @ApiModelProperty(value = "销售规则ID")
    private Long ruleId;

    @ApiModelProperty(value = "销售规则名称")
    private String ruleName;

}
