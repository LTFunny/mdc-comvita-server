package com.aquilaflycloud.mdc.result.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * MemberSignDescInfoResult
 *
 * @author star
 * @date 2020-01-03
 */
@Data
public class MemberSignDescInfoResult implements Serializable {
    @ApiModelProperty(value = "总签到天数")
    private Integer totalSign;

    @ApiModelProperty(value = "连续签到天数")
    private Integer continueSign;

    @ApiModelProperty(value = "签到日期")
    private List<String> signDateStrList;

    @ApiModelProperty(value = "签到奖励规则列表")
    private List<SignRewardRuleResult> rewardRule;
}
