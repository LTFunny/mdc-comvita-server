package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class QrCodeMsgForScanAddParam {
    @ApiModelProperty(value = "微信公众号appId", required = true)
    @NotBlank(message = "微信公众号appId")
    private String appId;

    @ApiModelProperty(value = "二维码自定义参数", required = true)
    @NotBlank(message = "自定义参数不能为空")
    private String sceneString;

    @ApiModelProperty(value = "二维码有效时间(秒)")
    private Integer expireSeconds = 2592000;
}
