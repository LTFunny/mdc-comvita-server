package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class WechatAuthorSiteGetParam {
    @ApiModelProperty(value = "微信appId", required = true)
    @NotBlank(message = "微信appId不能为空")
    private String appId;
}
