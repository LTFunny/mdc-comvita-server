package com.aquilaflycloud.mdc.result.ad;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * StatisticsResult
 *
 * @author star
 * @date 2019-11-18
 */
@Data
public class StatisticsResult {
    @ApiModelProperty(value = "广告总数")
    private Long total;

    @ApiModelProperty(value = "有效广告总数")
    private Long effectiveTotal;

    @ApiModelProperty(value = "推荐有效广告总数")
    private Long bannerEffectiveTotal;

    @ApiModelProperty(value = "弹出有效广告总数")
    private Long popupsEffectiveTotal;
}
