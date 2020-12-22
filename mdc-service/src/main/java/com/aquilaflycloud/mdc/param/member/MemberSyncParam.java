package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class MemberSyncParam {
    @ApiModelProperty(value = "微信公众号appId", required = true)
    @NotNull(message = "微信公众号appId不能为空")
    private String appId;
}