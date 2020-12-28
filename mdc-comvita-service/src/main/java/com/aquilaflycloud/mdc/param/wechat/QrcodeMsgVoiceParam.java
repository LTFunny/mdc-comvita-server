package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class QrcodeMsgVoiceParam {
    @ApiModelProperty(value = "多媒体文件id", required = true)
    @NotBlank(message = "多媒体文件id不能为空")
    private String mediaId;
}
