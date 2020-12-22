package com.aquilaflycloud.mdc.result.sign;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * OfflineSignResult
 *
 * @author star
 * @date 2020-05-07
 */
@Data
public class OfflineSignRewardResult implements Serializable {
    @ApiModelProperty(value = "打卡奖励")
    private SignReward signReward;

    @ApiModelProperty(value = "优惠券")
    private SignCoupon signCoupon;
}
