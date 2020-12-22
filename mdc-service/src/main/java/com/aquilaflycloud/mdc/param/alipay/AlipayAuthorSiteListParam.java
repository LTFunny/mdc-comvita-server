package com.aquilaflycloud.mdc.param.alipay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AlipayAuthorSiteListParam {
    @ApiModelProperty(value = "应用名")
    private String appName;
}
