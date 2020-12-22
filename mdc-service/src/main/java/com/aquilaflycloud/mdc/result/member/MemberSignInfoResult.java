package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.model.member.MemberSignRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * MemberSignInfoResult
 *
 * @author star
 * @date 2020-01-03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MemberSignInfoResult extends MemberSignRecord implements Serializable {
    @ApiModelProperty(value = "总签到天数")
    private Integer totalSign;

    @ApiModelProperty(value = "连续签到天数")
    private Integer continueSign;

    @ApiModelProperty(value = "奖励内容")
    private List<MemberRewardResult> rewardList;
}
