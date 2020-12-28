package com.aquilaflycloud.mdc.param.mission;

import com.aquilaflycloud.mdc.enums.mission.MissionTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * MissionAddParam
 *
 * @author star
 * @date 2020-05-08
 */
@Data
@Accessors(chain = true)
public class MissionAddParam {
    @ApiModelProperty(value = "任务类型", required = true)
    @NotNull(message = "任务类型不能为空")
    private MissionTypeEnum missionType;

    @ApiModelProperty(value = "图片路径")
    private String imgUrl;

    @ApiModelProperty(value = "任务规则列表", required = true)
    @NotEmpty(message = "任务规则列表不能为空")
    @Valid
    private List<MissionRuleParam> missionRuleList;
}


