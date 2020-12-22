package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class QrcodeMsgNewsParam {
    @ApiModelProperty(value = "图文消息标题", required = true)
    @NotBlank(message = "图文消息标题不能为空")
    private String title;

    @ApiModelProperty(value = "图文消息描述", required = true)
    @NotBlank(message = "图文消息描述不能为空")
    private String description;

    @ApiModelProperty(value = "图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200", required = true)
    @NotBlank(message = "图片链接不能为空")
    private String picUrl;

    @ApiModelProperty(value = "点击图文消息跳转链接", required = true)
    @NotBlank(message = "跳转链接不能为空")
    private String url;
}
