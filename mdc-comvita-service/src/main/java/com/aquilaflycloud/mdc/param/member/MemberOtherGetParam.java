package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MemberOtherGetParam {
    @ApiModelProperty(value = "是否获取未使用优惠券数量(默认false)")
    private Boolean getCouponCount = false;

    @ApiModelProperty(value = "是否获取未使用停车券数量(默认false)")
    private Boolean getParkingCouponCount = false;

    @ApiModelProperty(value = "等级奖励类型(member.RewardTypeEnum)")
    private RewardTypeEnum gradeType;

    @ApiModelProperty(value = "等级占比奖励类型(member.RewardTypeEnum)")
    private RewardTypeEnum gradeRateType;

    @ApiModelProperty(value = "排名奖励类型(member.RewardTypeEnum)")
    private RewardTypeEnum rankType;

    @ApiModelProperty(value = "总值奖励类型(member.RewardTypeEnum)")
    private RewardTypeEnum totalType;
}