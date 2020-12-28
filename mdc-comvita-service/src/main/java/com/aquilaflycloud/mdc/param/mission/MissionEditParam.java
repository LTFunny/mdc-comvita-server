package com.aquilaflycloud.mdc.param.mission;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * MissionEditParam
 *
 * @author star
 * @date 2020-05-08
 */
@Data
@Accessors(chain = true)
public class MissionEditParam {
    @ApiModelProperty(value = "任务id", required = true)
    @NotNull(message = "任务id不能为空")
    private Long id;

    @ApiModelProperty(value = "图片路径")
    private String imgUrl;

    @ApiModelProperty(value = "任务规则列表")
    @Valid
    private List<MissionRuleParam> missionRuleList;
}


