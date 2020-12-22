package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class TinyMemberLoginParam implements Serializable {
    @ApiModelProperty(value = "小程序用户授权码", required = true)
    @NotBlank(message = "authCode不能为空")
    private String authCode;
}
