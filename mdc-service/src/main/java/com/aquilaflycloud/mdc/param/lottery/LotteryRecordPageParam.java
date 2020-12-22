package com.aquilaflycloud.mdc.param.lottery;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.lottery.LotteryMemberRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * RecordPageParam
 *
 * @author star
 * @date 2020-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryRecordPageParam extends PageParam<LotteryMemberRecord> {
    @ApiModelProperty(value = "抽奖活动id", required = true)
    @NotNull(message = "抽奖活动id不能为空")
    private Long lotteryId;
}


