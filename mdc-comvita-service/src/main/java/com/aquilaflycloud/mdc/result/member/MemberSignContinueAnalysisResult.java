package com.aquilaflycloud.mdc.result.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * MemberSignContinueAnalysisResult
 *
 * @author star
 * @date 2020-01-03
 */
@Data
public class MemberSignContinueAnalysisResult implements Serializable {
    @ApiModelProperty(value = "连续签到次数范围")
    private String rangStr;

    @ApiModelProperty(value = "签到数")
    private Long signCount;

    @ApiModelProperty(value = "签到率")
    private String signRate;
}
