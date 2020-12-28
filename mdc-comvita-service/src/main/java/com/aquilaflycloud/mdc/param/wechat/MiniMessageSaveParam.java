package com.aquilaflycloud.mdc.param.wechat;

import com.aquilaflycloud.mdc.enums.wechat.MiniMessageLangEnum;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageStateEnum;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageTmplTypeEnum;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class MiniMessageSaveParam {
    @ApiModelProperty(value = "微信小程序appId", required = true)
    @NotBlank(message = "微信小程序appId不能为空")
    private String appId;

    @ApiModelProperty(value = "消息类型(wechat.MiniMessageTypeEnum)", required = true)
    @NotNull(message = "消息类型不能为空")
    private MiniMessageTypeEnum messageType;

    @ApiModelProperty(value = "小程序模板id", required = true)
    @NotBlank(message = "小程序模板id不能为空")
    private String priTmplId;

    @ApiModelProperty(value = "模板标题", required = true)
    @NotBlank(message = "模板标题不能为空")
    private String title;

    @ApiModelProperty(value = "模板内容", required = true)
    @NotBlank(message = "模板内容不能为空")
    private String content;

    @ApiModelProperty(value = "模板内容示例", required = true)
    @NotBlank(message = "模板内容示例不能为空")
    private String example;

    @ApiModelProperty(value = "模板类型(wechat.MiniMessageTmplTypeEnum)", required = true)
    @NotNull(message = "模板类型不能为空")
    private MiniMessageTmplTypeEnum type;

    @ApiModelProperty(value = "小程序页面路径", required = true)
    @NotBlank(message = "小程序页面路径不能为空")
    private String pagePath;

    @ApiModelProperty(value = "小程序版本(wechat.MiniMessageStateEnum)", required = true)
    @NotNull(message = "小程序版本不能为空")
    private MiniMessageStateEnum miniState;

    @ApiModelProperty(value = "进入小程序查看的语言类型(wechat.MiniMessageLangEnum)", required = true)
    @NotNull(message = "语言类型不能为空")
    private MiniMessageLangEnum miniLang;
}
