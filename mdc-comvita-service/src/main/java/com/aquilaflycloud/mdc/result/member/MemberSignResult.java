package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.model.member.MemberSignRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * MemberSignResult
 *
 * @author star
 * @date 2020-01-02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MemberSignResult extends MemberSignRecord implements Serializable {
    @ApiModelProperty(value = "奖励内容")
    private List<MemberRewardResult> rewardList;
}
