package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HealthQuickAddParam {
    @ApiModelProperty(value = "小程序码参数")
    private String sceneStr;
}