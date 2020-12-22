package com.aquilaflycloud.mdc.result.recommendation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * EventStatisticsResult
 *
 * @author star
 * @date 2020-03-28
 */
@Data
public class EventStatisticsResult implements Serializable {
    @ApiModelProperty(value = "浏览次数")
    private Long readPv = 0L;

    @ApiModelProperty(value = "浏览人数")
    private Long readUv = 0L;

    @ApiModelProperty(value = "点击次数")
    private Long clickPv = 0L;

    @ApiModelProperty(value = "点击人数")
    private Long clickUv = 0L;

    @ApiModelProperty(value = "分享次数")
    private Long sharePv = 0L;

    @ApiModelProperty(value = "分享人数")
    private Long shareUv = 0L;
}
