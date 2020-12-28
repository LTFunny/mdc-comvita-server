package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MiniMemberLoginParam implements Serializable {
    @ApiModelProperty(value = "小程序用户登录凭证", required = true)
    @NotBlank(message = "jsCode不能为空")
    private String jsCode;

}
