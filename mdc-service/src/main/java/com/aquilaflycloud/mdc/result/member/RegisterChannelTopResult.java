package com.aquilaflycloud.mdc.result.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * RegisterChannelTopResult
 *
 * @author star
 * @date 2020-02-20
 */
@Data
public class RegisterChannelTopResult implements Serializable {
    @ApiModelProperty(value = "渠道id")
    private Long channelId;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "会员数量")
    private Integer memberCount;
}
