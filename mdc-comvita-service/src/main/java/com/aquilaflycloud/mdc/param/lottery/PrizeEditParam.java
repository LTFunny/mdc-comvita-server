package com.aquilaflycloud.mdc.param.lottery;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * PrizeAddParam
 *
 * @author star
 * @date 2020-04-06
 */
@Data
@Accessors(chain = true)
public class PrizeEditParam {
    @ApiModelProperty(value = "奖品id", required = true)
    @NotNull(message = "奖品id不能为空")
    private Long id;

    @ApiModelProperty(value = "奖品等级")
    private String prizeLevel;

    @ApiModelProperty(value = "奖品图片")
    private String prizeImg;

    @ApiModelProperty(value = "奖品排序")
    private Integer prizeOrder;

    @ApiModelProperty(value = "关联奖品id")
    private Long relId;

    @ApiModelProperty(value = "奖励类型(member.RewardTypeEnum)")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "奖励值")
    @Min(value = 1, message = "消耗奖励值不能小于1")
    private Integer rewardValue;

    @ApiModelProperty(value = "奖品增加库存")
    @Min(value = 0, message = "奖品库存不能小于0")
    private Integer inventoryIncrease;

    @ApiModelProperty(value = "抽奖算法内容")
    @Valid
    private AlgorithmParam algorithm;
}


