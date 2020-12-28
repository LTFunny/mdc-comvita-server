package com.aquilaflycloud.mdc.param.lottery;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.lottery.FreeLimitEnum;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "consumeReward", notNullFieldName = "rewardType", message = "消耗奖励类型不能为空"),
        @AnotherFieldHasValue(fieldName = "totalLimit", fieldValue = "true", notNullFieldName = "totalCount", message = "累计限制次数不能为空"),
        @AnotherFieldHasValue(fieldName = "dailyLimit", fieldValue = "true", notNullFieldName = "dailyCount", message = "每日限制次数不能为空"),
        @AnotherFieldHasValue(fieldName = "freeLimit", fieldValue = "NONE", canNullFieldName = "freeLimitContent", message = "免费抽奖条件内容不能为空"),
})
@Data
public class LotteryRuleParam {
    @ApiModelProperty(value = "消耗奖励类型(member.RewardTypeEnum)")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "消耗奖励值")
    @Min(value = 0, message = "消耗奖励值不能小于0")
    private Integer consumeReward = 0;

    @ApiModelProperty(value = "免费抽奖条件类型(lottery.FreeLimitEnum)")
    private FreeLimitEnum freeLimit = FreeLimitEnum.NONE;

    @ApiModelProperty(value = "免费抽奖条件内容")
    private String freeLimitContent;

    @ApiModelProperty(value = "是否限制累计次数(默认false)")
    private Boolean totalLimit = false;

    @ApiModelProperty(value = "累计限制次数")
    @Min(value = 1, message = "累计限制次数不能小于1")
    private Integer totalCount;

    @ApiModelProperty(value = "是否限制每日次数(默认false)")
    private Boolean dailyLimit = false;

    @ApiModelProperty(value = "每日限制次数")
    @Min(value = 1, message = "每日限制次数不能小于1")
    private Integer dailyCount;
}
