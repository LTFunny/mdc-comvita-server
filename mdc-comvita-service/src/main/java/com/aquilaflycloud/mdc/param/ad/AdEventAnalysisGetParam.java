package com.aquilaflycloud.mdc.param.ad;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * AdEventAnalysisGetParam
 *
 * @author star
 * @date 2019-11-22
 */
@Data
@Accessors(chain = true)
public class AdEventAnalysisGetParam {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "广告id不能为空")
    private Long id;

    @ApiModelProperty(value = "事件开始时间", required = true)
    @NotNull(message = "开始时间不能为空")
    private Date startTime;

    @ApiModelProperty(value = "事件结束时间", required = true)
    @NotNull(message = "结束时间不能为空")
    private Date endTime;

    @ApiModelProperty(value = "分析类型(SHOW_PV,SHOW_UV,CLICK_PV,CLICK_UV)", required = true)
    @NotNull(message = "分析类型不能为空")
    private AdAnalysisType analysisType;

    public enum AdAnalysisType {
        // 分析类型
        SHOW_PV, SHOW_UV, CLICK_PV, CLICK_UV
    }
}


