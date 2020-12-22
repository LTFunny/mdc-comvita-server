package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class CodeGetParam {

    @ApiModelProperty(value = "小程序appId", required = true)
    @NotBlank(message = "appId不能为空")
    private String appId;

}
