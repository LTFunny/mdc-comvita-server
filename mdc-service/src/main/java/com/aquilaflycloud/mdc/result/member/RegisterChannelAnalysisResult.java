package com.aquilaflycloud.mdc.result.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * RegisterChannelAnalysisResult
 *
 * @author star
 * @date 2020-02-20
 */
@Data
public class RegisterChannelAnalysisResult implements Serializable {
    @ApiModelProperty(value = "分析日期")
    private String channelDate;

    @ApiModelProperty(value = "会员数量")
    private Integer memberCount;
}
