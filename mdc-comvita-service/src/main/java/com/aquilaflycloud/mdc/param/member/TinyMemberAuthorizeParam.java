package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class TinyMemberAuthorizeParam implements Serializable {
    @ApiModelProperty(value = "用户信息对象", required = true)
    @NotNull(message = "authCode不能为空")
    private AlipayUserInfo userInfo;
}