package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MemberPhoneEditParam implements Serializable {

    @ApiModelProperty(value = "手机号码", required = true)
    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;

    @ApiModelProperty(value = "手机短信验证码", required = true)
    @NotBlank(message = "验证码不能为空")
    private String msgCode;

}
