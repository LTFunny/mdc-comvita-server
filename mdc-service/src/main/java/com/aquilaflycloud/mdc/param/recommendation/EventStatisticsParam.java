package com.aquilaflycloud.mdc.param.recommendation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * RecommendationStatisticsParam
 *
 * @author star
 * @date 2020-03-28
 */
@Data
public class EventStatisticsParam {
    @ApiModelProperty(value = "最新推荐id", required = true)
    @NotNull(message = "最新推荐id不能为空")
    private Long id;

    @ApiModelProperty(value = "事件开始时间")
    private Date startTime;

    @ApiModelProperty(value = "事件结束时间")
    private Date endTime;
}
