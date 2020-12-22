package com.aquilaflycloud.mdc.result.lottery;

import com.aquilaflycloud.mdc.enums.lottery.FreeLimitEnum;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LotteryRule {
    @ApiModelProperty(value = "消耗奖励类型")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "消耗奖励值")
    private Integer consumeReward;

    @ApiModelProperty(value = "免费抽奖条件类型")
    private FreeLimitEnum freeLimit;

    @ApiModelProperty(value = "免费抽奖条件内容")
    private String freeLimitContent;

    @ApiModelProperty(value = "是否限制累计次数")
    private Boolean totalLimit;

    @ApiModelProperty(value = "累计限制次数")
    private Integer totalCount;

    @ApiModelProperty(value = "是否限制每日次数")
    private Boolean dailyLimit;

    @ApiModelProperty(value = "每日限制次数")
    private Integer dailyCount;
}
