package com.aquilaflycloud.mdc.result.mission;

import com.aquilaflycloud.mdc.model.mission.MissionInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * MissionInfoResult
 *
 * @author star
 * @date 2020-05-08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MissionInfoResult extends MissionInfo {
    @ApiModelProperty(value = "任务规则列表")
    private List<MissionRuleResult> missionRuleList;
}
