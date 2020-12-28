package com.aquilaflycloud.mdc.result.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * MemberEventStatisticsResult
 *
 * @author star
 * @date 2019-11-21
 */
@Data
public class MemberEventAnalysisResult implements Serializable {
    @ApiModelProperty(value = "触发事件时间(yyyy-MM-dd)")
    private String eventDate;

    @ApiModelProperty(value = "触发事件数")
    private Long eventCount;
}
