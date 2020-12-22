package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MiniPluginMemberRegisterParam implements Serializable {
    @ApiModelProperty(value = "小程序插件会员id", required = true)
    @NotNull(message = "pluginMemberId不能为空")
    private Long pluginMemberId;
}