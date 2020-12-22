package com.aquilaflycloud.mdc.result.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChannelInfoRefIdResult {
    @ApiModelProperty(value = "最新推荐id")
    private Long refId;
}
