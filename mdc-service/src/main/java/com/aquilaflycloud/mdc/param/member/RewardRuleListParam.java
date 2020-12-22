package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.mdc.enums.member.RuleTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class RewardRuleListParam {
    @ApiModelProperty(value = "奖励规则类型列表(member.RuleTypeEnum)")
    private List<RuleTypeEnum> ruleTypeList;

    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;
}