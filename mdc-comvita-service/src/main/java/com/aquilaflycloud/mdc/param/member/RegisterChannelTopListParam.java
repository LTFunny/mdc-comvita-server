package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegisterChannelTopListParam {
    @ApiModelProperty(value = "微信appId")
    private String appId;

    @ApiModelProperty(value = "限制数量")
    private Integer limit = 10;
}