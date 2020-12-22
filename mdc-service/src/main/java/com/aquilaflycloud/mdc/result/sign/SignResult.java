package com.aquilaflycloud.mdc.result.sign;

import com.aquilaflycloud.mdc.model.sign.OfflineSignActivity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SignResult
 *
 * @author star
 * @date 2020-05-07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SignResult extends OfflineSignActivity {
    @ApiModelProperty(value = "打卡奖励")
    private SignReward signReward;

    @ApiModelProperty(value = "优惠券")
    private SignCoupon signCoupon;

    @ApiModelProperty(value = "打卡次数")
    private Integer signCount;

    @ApiModelProperty(value = "打卡人数")
    private Integer signMember;

    @ApiModelProperty(value = "昨日打卡次数")
    private Integer yesterdaySignCount;

    @ApiModelProperty(value = "昨日打卡人数")
    private Integer yesterdaySignMember;
}
