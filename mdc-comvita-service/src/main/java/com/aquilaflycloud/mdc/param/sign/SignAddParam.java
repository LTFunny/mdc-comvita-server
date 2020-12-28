package com.aquilaflycloud.mdc.param.sign;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import com.aquilaflycloud.mdc.enums.sign.LimitTypeEnum;
import com.aquilaflycloud.mdc.enums.sign.SignRewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * SignAddParam
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
public class SignAddParam {
    @ApiModelProperty(value = "微信或支付宝appId", required = true)
    @NotBlank(message = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value = "打卡活动名称", required = true)
    @NotNull(message = "打卡活动名称不能为空")
    private String signName;

    @ApiModelProperty(value = "打卡活动详情")
    private String signDetail;

    @ApiModelProperty(value = "打卡开始时间", required = true)
    @NotNull(message = "打卡开始时间不能为空")
    private Date startTime;

    @ApiModelProperty(value = "打卡结束时间", required = true)
    @NotNull(message = "打卡结束时间不能为空")
    private Date endTime;

    @ApiModelProperty(value = "活动图片", required = true)
    @NotNull(message = "活动图片不能为空")
    private String imgUrl;

    @ApiModelProperty(value = "打卡限制类型(sign.LimitTypeEnum)", required = true)
    @NotNull(message = "打卡限制类型不能为空")
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


