package com.aquilaflycloud.mdc.result.lottery;

import com.aquilaflycloud.mdc.model.lottery.LotteryMemberRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * RecordResult
 *
 * @author star
 * @date 2020-04-07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecordResult extends LotteryMemberRecord {
    @ApiModelProperty(value = "优惠券记录id")
    private Long couponRelId;
}
