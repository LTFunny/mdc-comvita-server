package com.aquilaflycloud.mdc.result.sign;

import com.aquilaflycloud.mdc.model.sign.OfflineSignMemberRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OfflineSignRecordResult
 *
 * @author star
 * @date 2020-06-08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OfflineSignRecordResult extends OfflineSignMemberRecord {
    @ApiModelProperty(value = "打卡活动奖励")
    private OfflineSignRewardResult signReward;
}
