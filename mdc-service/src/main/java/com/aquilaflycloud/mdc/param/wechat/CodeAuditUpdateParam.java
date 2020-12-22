package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CodeAuditUpdateParam {

    @ApiModelProperty(value = "小程序代码id")
    private String id;
}
