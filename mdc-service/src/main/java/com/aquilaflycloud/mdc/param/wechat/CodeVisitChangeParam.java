package com.aquilaflycloud.mdc.param.wechat;

import com.aquilaflycloud.mdc.enums.wechat.CodeVisitEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class CodeVisitChangeParam {

    @ApiModelProperty(value = "小程序appId", required = true)
    @NotBlank(message = "小程序appId不能为空")
    private String appId;

    @ApiModelProperty(value = "操作类型(wechat.CodeVisitEnum)", required = true)
    @NotNull(message = "操作类型不能为空")
    private CodeVisitEnum action;

}
