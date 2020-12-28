package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class RegisterChannelGetParam {
    @ApiModelProperty(value = "渠道id", required = true)
    @NotNull(message = "渠道id不能为空")
    private Long id;
}