package com.aquilaflycloud.mdc.result.ad;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * EventStatisticsResult
 * 
 * @author star
 * @date 2019-11-21
 */
@Data
public class EventStatisticsResult {
    @ApiModelProperty(value = "广告展示次数")
    private Long showPv = 0L;

    @ApiModelProperty(value = "广告展示人数")
    private Long showUv = 0L;

    @ApiModelProperty(value = "广告点击次数")
    private Long clickPv = 0L;

    @ApiModelProperty(value = "广告点击人数")
    private Long clickUv = 0L;

    @ApiModelProperty(value = "广告点击转化率")
    private String conversionRate = NumberUtil.formatPercent(0, 2);
}
