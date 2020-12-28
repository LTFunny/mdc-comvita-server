package com.aquilaflycloud.mdc.param.recommendation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * EventAnalysisGetParam
 *
 * @author star
 * @date 2020-03-28
 */
@Data
@Accessors(chain = true)
public class EventAnalysisGetParam {
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "推荐id不能为空")
    private Long id;

    @ApiModelProperty(value = "事件开始时间", required = true)
    @NotNull(message = "开始时间不能为空")
    private Date startTime;

    @ApiModelProperty(value = "事件结束时间", required = true)
    @NotNull(message = "结束时间不能为空")
    private Date endTime;

    @ApiModelProperty(value = "分析类型(READ_PV, READ_UV, CLICK_PV, CLICK_UV, SHARE_PV, SHARE_UV)", required = true)
    @NotNull(message = "分析类型不能为空")
    private RecommendAnalysisTypeEnum analysisType;

    public enum RecommendAnalysisTypeEnum {
        // 分析类型
        READ_PV, READ_UV, CLICK_PV, CLICK_UV, SHARE_PV, SHARE_UV
    }
}
