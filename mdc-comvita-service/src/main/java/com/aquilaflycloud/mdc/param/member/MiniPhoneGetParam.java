package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MiniPhoneGetParam implements Serializable {

    @ApiModelProperty(value = "包括敏感数据在内的完整用户信息的加密数据", required = true)
    @NotBlank(message = "encryptedData不能为空")
    private String encryptedData;

    @ApiModelProperty(value = "加密算法的初始向量", required = true)
    @NotBlank(message = "iv不能为空")
    private String iv;

}
