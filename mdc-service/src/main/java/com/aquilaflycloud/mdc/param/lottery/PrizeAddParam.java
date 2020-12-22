package com.aquilaflycloud.mdc.param.lottery;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.lottery.PrizeTypeEnum;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * PrizeAddParam
 *
 * @author star
 * @date 2020-04-06
 */
@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "prizeType", fieldValue = "REWARD", canNullFieldName = "relId", message = "关联奖品id不能为空"),
        @AnotherFieldHasValue(fieldName = "prizeType", fieldValue = "REWARD", notNullFieldName = "rewardType", message = "奖励类型不能为空"),
        @AnotherFieldHasValue(fieldName = "prizeType", fieldValue = "REWARD", notNullFieldName = "rewardValue", message = "奖励值不能为空"),
})
@Data
@Accessors(chain = true)
public class PrizeAddParam {
    @ApiModelProperty(value = "奖品等级", required = true)
    @NotBlank(message = "奖品等级不能为空")
    private String prizeLevel;

    @ApiModelProperty(value = "奖品图片", required = true)
    @NotBlank(message = "奖品图片不能为空")
    private String prizeImg;

    @ApiModelProperty(value = "奖品排序", required = true)
    @NotNull(message = "奖品排序不能为空")
    private Integer prizeOrder;

    @ApiModelProperty(value = "奖品类型(lottery.PrizeTypeEnum)", required = true)
    @NotNull(message = "奖品类型不能为空")
    private PrizeTypeEnum prizeType;

    @ApiModelProperty(value = "关联奖品id")
    private Long relId;

    @ApiModelProperty(value = "奖励类型(member.RewardTypeEnum)")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "奖励值")
    @Min(value = 1, message = "消耗奖励值不能小于1")
    private Integer rewardValue;

    @ApiModelProperty(value = "奖品库存", required = true)
    @NotNull(message = "奖品库存不能为空")
    @Min(value = 1, message = "奖品库存不能小于1")
    private Integer inventory;

    @ApiModelProperty(value = "抽奖算法内容")
    @Valid
    private AlgorithmParam algorithm;
}


