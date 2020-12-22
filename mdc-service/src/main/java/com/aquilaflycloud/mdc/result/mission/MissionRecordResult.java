package com.aquilaflycloud.mdc.result.mission;

import com.aquilaflycloud.mdc.model.mission.MissionMemberRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * MissionRecordResult
 *
 * @author star
 * @date 2020-05-09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MissionRecordResult extends MissionMemberRecord {
    @ApiModelProperty(value = "任务规则")
    private MissionRuleResult missionRule;
}
