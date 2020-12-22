package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class MemberInfoGetParam {
    @ApiModelProperty(value = "会员码", required = true)
    @NotBlank(message = "会员码不能为空")
    private String memberCode;
}
