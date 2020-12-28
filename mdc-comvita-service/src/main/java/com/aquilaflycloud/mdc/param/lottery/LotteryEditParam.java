package com.aquilaflycloud.mdc.param.lottery;

import com.aquilaflycloud.mdc.enums.lottery.LotteryModeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
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
public class LotteryEditParam {
    @ApiModelProperty(value = "抽奖活动id", required = true)
    @NotNull(message = "抽奖活动id不能为空")
    private Long id;

    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value = "抽奖活动名称")
    private String lotteryName;

    @ApiModelProperty(value = "抽奖开始时间")
    private Date startTime;

    @ApiModelProperty(value = "抽奖结束时间")
    private Date endTime;

    @ApiModelProperty(value = "抽奖模板(lottery.LotteryModeEnum)")
    private LotteryModeEnum lotteryMode;

    @ApiModelProperty(value = "抽奖详情")
    private String lotteryRemark;

    @ApiModelProperty(value = "抽奖规则")
    @Valid
    private LotteryRuleParam lotteryRule;

    @ApiModelProperty(value = "抽奖算法")
    @Valid
    private AlgorithmParam algorithm;

    @ApiModelProperty(value = "新增奖品列表")
    @Valid
    private List<PrizeAddParam> prizeAddList;

    @ApiModelProperty(value = "编辑奖品列表")
    @Valid
    private List<PrizeEditParam> prizeEditList;

    @ApiModelProperty(value = "删除奖品列表")
    @Valid
    private List<Long> prizeDeleteList;
}


