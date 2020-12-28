package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class QrcodeMsgVideoParam {
    @ApiModelProperty(value = "多媒体文件id", required = true)
    @NotBlank(message = "多媒体文件id不能为空")
    private String mediaId;

    @ApiModelProperty(value = "视频消息的标题")
    private String title;

    @ApiModelProperty(value = "视频消息的描述")
    private String description;
}
