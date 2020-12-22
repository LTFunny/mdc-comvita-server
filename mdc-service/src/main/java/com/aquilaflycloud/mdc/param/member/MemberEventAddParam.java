package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class MemberEventAddParam {

    @ApiModelProperty(value = "业务数据id", required = true)
    @NotNull(message = "业务数据id不能为空")
    private Long businessId;
}