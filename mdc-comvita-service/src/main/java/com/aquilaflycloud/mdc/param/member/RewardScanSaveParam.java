package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class RewardScanSaveParam extends RewardSaveParam {
    @ApiModelProperty(value = "消费金额", required = true)
    @DecimalMin(value = "0.01", message = "消费金额不能小于0.01")
    @NotNull(message = "消费金额不能为空")
    private BigDecimal consumeMoney;

    @ApiModelProperty(value = "满足消费金额奖励", required = true)
    @Min(value = 0, message = "奖励不能小于0")
    @NotNull(message = "奖励不能为空")
    private Integer scanReward;

    @ApiModelProperty(value = "每次最大奖励")
    private Integer preMaxReward;

    @ApiModelProperty(value = "每天最大奖励")
    private Integer dayMaxReward;

    @ApiModelProperty(value = "额外奖励")
    @Valid
    private List<ExtReward> extRewardList;

    @ApiModelProperty(value = "业态奖励")
    @Valid
    private List<FormatReward> formatRewardList;

    @Data
    private class ExtReward {
        @ApiModelProperty(value = "额外消费金额", required = true)
        @DecimalMin(value = "0.01", message = "消费金额不能小于0.01")
        @NotNull(message = "消费金额不能为空")
        private BigDecimal extConsumeMoney;

        @ApiModelProperty(value = "满足额外消费金额奖励", required = true)
        @Min(value = 0, message = "奖励不能小于0")
        @NotNull(message = "奖励不能为空")
        private Integer extReward;
    }

    @Data
    private class FormatReward {
        @ApiModelProperty(value = "业态id", required = true)
        @NotNull(message = "业态id不能为空")
        private Long formatId;

        @ApiModelProperty(value = "业态名称", required = true)
        @NotEmpty(message = "业态名称不能为空")
        private String formatName;

        @ApiModelProperty(value = "业态消费金额", required = true)
        @DecimalMin(value = "0.01", message = "业态消费金额不能小于0.01")
        @NotNull(message = "业态消费金额不能为空")
        private BigDecimal formatConsumeMoney;

        @ApiModelProperty(value = "业态消费金额奖励", required = true)
        @Min(value = 0, message = "业态奖励不能小于0")
        @NotNull(message = "业态奖励不能为空")
        private Integer formatReward;
    }
}
