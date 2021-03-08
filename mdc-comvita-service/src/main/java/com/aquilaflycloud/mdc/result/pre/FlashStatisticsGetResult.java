package com.aquilaflycloud.mdc.result.pre;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * FlashStatisticsGetResult
 *
 * @author star
 * @date 2021/3/8
 */
@Data
public class FlashStatisticsGetResult {
    @ApiModelProperty(value = "参与人数")
    private Long participantsCount;

    @ApiModelProperty(value = "分享次数")
    private Long sharePv = 0L;

    @ApiModelProperty(value = "分享人数")
    private Long shareUv = 0L;

    @ApiModelProperty(value = "点击次数")
    private Long clickPv = 0L;

    @ApiModelProperty(value = "点击人数")
    private Long clickUv = 0L;

    @ApiModelProperty(value = "参加转化率")
    private String conversionRate = NumberUtil.formatPercent(0, 2);

}
