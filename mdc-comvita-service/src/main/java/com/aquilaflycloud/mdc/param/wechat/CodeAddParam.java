package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class CodeAddParam {

    @ApiModelProperty(value = "小程序appId", required = true)
    @NotBlank(message = "appId不能为空")
    private String appId;

    @ApiModelProperty(value = "代码版本号", required = true)
    @NotBlank(message = "代码版本号不能为空")
    private String codeVersion;

    @ApiModelProperty(value = "代码描述", required = true)
    @NotBlank(message = "代码描述不能为空")
    private String codeDesc;

}
