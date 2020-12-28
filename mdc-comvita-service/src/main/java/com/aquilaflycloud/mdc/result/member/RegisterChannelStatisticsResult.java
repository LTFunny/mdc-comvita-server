package com.aquilaflycloud.mdc.result.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * RegisterChannelStatisticsResult
 *
 * @author star
 * @date 2020-02-20
 */
@Data
public class RegisterChannelStatisticsResult implements Serializable {
    @ApiModelProperty(value = "总会员数量")
    private Integer totalCount;

    @ApiModelProperty(value = "渠道会员数量")
    private Integer channelCount;

    @ApiModelProperty(value = "渠道会员数占比")
    private String percent;
}
