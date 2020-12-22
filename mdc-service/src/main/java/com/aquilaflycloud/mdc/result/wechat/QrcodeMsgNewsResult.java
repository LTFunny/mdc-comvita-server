package com.aquilaflycloud.mdc.result.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QrcodeMsgNewsResult {
    @ApiModelProperty(value = "图文消息标题")
    private String title;

    @ApiModelProperty(value = "图文消息描述")
    private String description;

    @ApiModelProperty(value = "图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200")
    private String picUrl;

    @ApiModelProperty(value = "点击图文消息跳转链接")
    private String url;
}
