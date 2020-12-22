package com.aquilaflycloud.mdc.param.lottery;

import com.aquilaflycloud.mdc.constant.MdcConstant;
import com.aquilaflycloud.mdc.enums.lottery.LotteryModeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * LotteryAddParam
 *
 * @author star
 * @date 2020-04-06
 */
@Data
@Accessors(chain = true)
public class LotteryAddParam {
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId = MdcConstant.UNIVERSAL_APP_ID;

    @ApiModelProperty(value = "抽奖活动名称", required = true)
    @NotBlank(message = "抽奖活动名称不能为空")
    private String lotteryName;

    @ApiModelProperty(value = "抽奖开始时间", required = true)
    @NotNull(message = "抽奖开始时间不能为空")
    private Date startTime;

    @ApiModelProperty(value = "抽奖结束时间", required = true)
    @NotNull(message = "抽奖结束时间不能为空")
    private Date endTime;

    @ApiModelProperty(value = "抽奖模板(lottery.LotteryModeEnum)", required = true)
    @NotNull(message = "抽奖模板不能为空")
    private LotteryModeEnum lotteryMode;

    @ApiModelProperty(value = "抽奖详情")
    private String lotteryRemark;

    @ApiModelProperty(value = "抽奖规则", required = true)
    @NotNull(message = "抽奖规则不能为空")
    @Valid
    private LotteryRuleParam lotteryRule;

    @ApiModelProperty(value = "抽奖算法", required = true)
    @NotNull(message = "抽奖算法不能为空")
    @Valid
    private AlgorithmParam algorithm;

    @ApiModelProperty(value = "奖品列表", required = true)
    @NotEmpty(message = "奖品列表不能为空")
    @Valid
    private List<PrizeAddParam> prizeList;
}


