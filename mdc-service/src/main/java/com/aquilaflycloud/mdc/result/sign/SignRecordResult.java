package com.aquilaflycloud.mdc.result.sign;

import com.aquilaflycloud.mdc.model.sign.OfflineSignActivity;
import com.aquilaflycloud.mdc.model.sign.OfflineSignMemberRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SignRecordResult
 *
 * @author star
 * @date 2020-05-08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SignRecordResult extends OfflineSignMemberRecord {
    @ApiModelProperty(value = "打卡活动详情")
    private OfflineSignActivity offlineSignActivity;
}
