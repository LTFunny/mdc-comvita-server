package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class MiniTemplateGetParam {
    @ApiModelProperty(value = "微信appId", required = true)
    @NotBlank(message = "微信appId不能为空")
    private String appId;

    @ApiModelProperty(value = "模板id", required = true)
    @NotBlank(message = "模板id不能为空")
    private String priTmplId;
}
