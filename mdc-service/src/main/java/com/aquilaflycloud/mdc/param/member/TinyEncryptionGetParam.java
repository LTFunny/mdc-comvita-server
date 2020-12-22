package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class TinyEncryptionGetParam implements Serializable {
    @ApiModelProperty(value = "报文（密文）", required = true)
    @NotBlank(message = "报文不能为空")
    private String response;

    @ApiModelProperty(value = "对报文的签名", required = true)
    @NotBlank(message = "签名不能为空")
    private String sign;

}
