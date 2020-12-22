package com.aquilaflycloud.mdc.param.lottery;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.lottery.LotteryModeEnum;
import com.aquilaflycloud.mdc.enums.lottery.LotteryStateEnum;
import com.aquilaflycloud.mdc.model.lottery.LotteryActivity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * LotteryPageParam
 *
 * @author star
 * @date 2020-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryPageParam extends PageParam<LotteryActivity> {
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value = "抽奖活动名称")
    private String lotteryName;

    @ApiModelProperty(value = "抽奖模板(lottery.LotteryModeEnum)")
    private LotteryModeEnum lotteryMode;

    @ApiModelProperty(value = "状态(lottery.LotteryStateEnum)")
    private LotteryStateEnum state;

    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(value = "创建开始时间")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建结束时间")
    private Date createTimeEnd;

    @ApiModelProperty(value = "是否查询有效状态(common.WhetherEnum)(默认否, 为是时state无效)")
    private WhetherEnum valid = WhetherEnum.NO;
}


