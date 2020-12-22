package com.aquilaflycloud.mdc.param.alipay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class AlipayAuthorSiteGetParam {
    @ApiModelProperty(value = "授权应用Id", required = true)
    @NotNull(message = "授权应用Id不能为空")
    private Long id;
}
