package com.aquilaflycloud.mdc.param.sign;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import com.aquilaflycloud.mdc.enums.sign.LimitTypeEnum;
import com.aquilaflycloud.mdc.enums.sign.SignRewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * SignEditParam
 *
 * @author star
 * @date 2020-05-07
 */
@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "signRewardType", fieldValue = "REWARD", canNullFieldName = "relId", message = "关联奖品id不能为空"),
        @AnotherFieldHasValue(fieldName = "signRewardType", fieldValue = "REWARD", notNullFieldName = "rewardType", message = "奖励类型不能为空"),
        @AnotherFieldHasValue(fieldName = "signRewardType", fieldValue = "REWARD", notNullFieldName = "rewardValue", message = "奖励值不能为空"),
})
@Data
@Accessors(chain = true)
public class SignEditParam {
    @ApiModelProperty(value = "打卡活动id", required = true)
    @NotNull(message = "打卡活动id不能为空")
    private Long id;

    @ApiModelProperty(value = "打卡活动名称")
    private String signName;

    @ApiModelProperty(value = "打卡活动详情")
    private String signDetail;

    @ApiModelProperty(value = "打卡开始时间")
    private Date startTime;

    @ApiModelProperty(value = "打卡结束时间")
    private Date endTime;

    @ApiModelProperty(value = "活动图片")
    private String imgUrl;

    @ApiModelProperty(value = "打卡限制类型(sign.LimitTypeEnum)")
    private LimitTypeEnum limitType;

    @ApiModelProperty(value = "打卡奖励类型(sign.SignRewardTypeEnum)", required = true)
    @NotNull(message = "打卡奖励类型不能为空")
    private SignRewardTypeEnum signRewardType;

    @ApiModelProperty(value = "关联奖品id")
    private Long relId;

    @ApiModelProperty(value = "奖励类型(member.RewardTypeEnum)")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "奖励值")
    @Min(value = 1, message = "奖励值不能小于1")
    private Integer rewardValue;

    @ApiModelProperty(value = "商户ids(多个以,分隔)")
    private String designateOrgIds;
}

