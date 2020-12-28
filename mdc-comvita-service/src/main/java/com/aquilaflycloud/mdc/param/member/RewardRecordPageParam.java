package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.member.RewardSourceEnum;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class RewardRecordPageParam extends PageParam {
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "奖励类型(member.RewardTypeEnum)")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @ApiModelProperty(value = "微信或支付宝昵称")
    private String nickName;

    @ApiModelProperty(value = "开始时间")
    private Date createTimeStart;

    @ApiModelProperty(value = "结束时间")
    private Date createTimeEnd;

    @ApiModelProperty(value = "奖励来源(member.RewardSourceEnum)")
    private RewardSourceEnum rewardSource;

    @ApiModelProperty(value = "最小奖励值")
    private Integer minRewardValue;

    @ApiModelProperty(value = "最大奖励值")
    private Integer maxRewardValue;

    @ApiModelProperty(value = "创建记录人名称")
    private String creatorName;
}