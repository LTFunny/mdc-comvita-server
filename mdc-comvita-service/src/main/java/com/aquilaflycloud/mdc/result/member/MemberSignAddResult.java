package com.aquilaflycloud.mdc.result.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * MemberSignAddResult
 *
 * @author star
 * @date 2020-01-03
 */
@Data
public class MemberSignAddResult implements Serializable {
    @ApiModelProperty(value = "签到奖励值列表")
    private List<MemberRewardResult> rewardResultList;
}
