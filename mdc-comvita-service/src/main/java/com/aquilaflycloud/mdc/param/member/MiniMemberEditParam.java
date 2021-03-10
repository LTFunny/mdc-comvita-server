package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MiniMemberEditParam implements Serializable {
    @ApiModelProperty(value = "用户信息对象，不包含 openid 等敏感信息", required = true)
    @NotNull(message = "userInfo不能为空")
    private WechatUserInfo userInfo;

    @ApiModelProperty(value = "导购员id")
    private Long guideId;
}
