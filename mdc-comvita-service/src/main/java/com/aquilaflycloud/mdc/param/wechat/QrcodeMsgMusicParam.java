package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class QrcodeMsgMusicParam {
    @ApiModelProperty(value = "音乐标题")
    private String title;

    @ApiModelProperty(value = "音乐描述")
    private String description;

    @ApiModelProperty(value = "音乐链接", required = true)
    @NotBlank(message = "音乐链接不能为空")
    private String musicUrl;

    @ApiModelProperty(value = "高质量音乐链接，WIFI环境优先使用该链接播放音乐")
    private String hqMusicUrl;

    @ApiModelProperty(value = "缩略图的媒体文件id", required = true)
    @NotBlank(message = "多媒体文件id不能为空")
    private String thumbMediaId;
}
