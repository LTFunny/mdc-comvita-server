package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientConfigListParam {
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;
}