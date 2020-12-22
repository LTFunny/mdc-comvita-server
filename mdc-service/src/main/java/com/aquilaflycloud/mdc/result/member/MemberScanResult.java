package com.aquilaflycloud.mdc.result.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * MemberScanResult
 *
 * @author star
 * @date 2020-03-27
 */
@Data
public class MemberScanResult implements Serializable {
    @ApiModelProperty(value = "奖励内容")
    private List<MemberScanRewardResult> rewardList;
}
