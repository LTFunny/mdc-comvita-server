package com.aquilaflycloud.mdc.result.sign;

import com.aquilaflycloud.mdc.model.sign.OfflineSignMemberRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * RecordResult
 *
 * @author star
 * @date 2020-05-07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecordResult extends OfflineSignMemberRecord {
    @ApiModelProperty(value = "优惠券记录id")
    private Long couponRelId;
}
