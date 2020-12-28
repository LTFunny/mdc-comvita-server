package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class MemberGradeGetParam {
    @ApiModelProperty(value = "会员等级id", required = true)
    @NotNull(message = "会员等级id不能为空")
    private Long id;
}