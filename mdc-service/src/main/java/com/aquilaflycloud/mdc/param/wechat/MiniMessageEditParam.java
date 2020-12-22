package com.aquilaflycloud.mdc.param.wechat;

import com.aquilaflycloud.mdc.enums.wechat.MiniMessageLangEnum;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class MiniMessageEditParam {
    @ApiModelProperty(value = "消息模板id", required = true)
    @NotNull(message = "消息模板id不能为空")
    private Long id;

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
