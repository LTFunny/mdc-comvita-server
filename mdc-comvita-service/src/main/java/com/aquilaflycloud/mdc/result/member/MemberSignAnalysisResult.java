package com.aquilaflycloud.mdc.result.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * MemberSignAnalysisResult
 *
 * @author star
 * @date 2020-01-02
 */
@Data
public class MemberSignAnalysisResult implements Serializable {
    @ApiModelProperty(value = "分析统计时间")
    private Date createDate;

    @ApiModelProperty(value = "签到数")
    private Long signCount;

    @ApiModelProperty(value = "签到率")
    private String signRate;
}
