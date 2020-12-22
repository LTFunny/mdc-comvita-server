package com.aquilaflycloud.mdc.result.apply;

import com.aquilaflycloud.mdc.model.apply.ApplyActivity;
import com.aquilaflycloud.mdc.model.apply.ApplyMemberRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ApplyResult
 *
 * @author star
 * @date 2020-02-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApplyMemberRecordResult extends ApplyMemberRecord {
    @ApiModelProperty(value = "报名活动信息")
    private ApplyActivity applyActivity;
}
