package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * ScanRewardRuleResult
 *
 * @author star
 * @date 2019-12-19
 */
@Data
public class ScanRewardRuleResult {
    @ApiModelProperty(value = "奖励类型")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "消费金额")
    private BigDecimal consumeMoney;

    @ApiModelProperty(value = "满足消费金额奖励")
    private Integer scanReward;

    @ApiModelProperty(value = "每次最大奖励")
    private Integer preMaxReward;

    @ApiModelProperty(value = "每天最大奖励")
    private Integer dayMaxReward;

    @ApiModelProperty(value = "额外奖励")
    private List<ExtReward> extRewardList;

    @ApiModelProperty(value = "业态奖励")
    private List<FormatReward> formatRewardList;

    @Data
    public class ExtReward {
        @ApiModelProperty(value = "额外消费金额")
        private BigDecimal extConsumeMoney;

        @ApiModelProperty(value = "满足额外消费金额奖励")
        private Integer extReward;
    }

    @Data
    public class FormatReward {
        @ApiModelProperty(value = "业态id")
        private Long formatId;

        @ApiModelProperty(value = "业态名称")
        private String formatName;

        @ApiModelProperty(value = "业态消费金额")
        private BigDecimal formatConsumeMoney;

        @ApiModelProperty(value = "业态消费金额奖励")
        private Integer formatReward;
    }
}
