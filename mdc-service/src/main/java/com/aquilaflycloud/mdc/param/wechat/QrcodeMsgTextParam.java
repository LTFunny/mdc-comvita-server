package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class QrcodeMsgTextParam {
    @ApiModelProperty(value = "回复的消息内容", required = true)
    @NotBlank(message = "消息内容不能为空")
    private String content;
}
