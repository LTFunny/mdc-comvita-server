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
public class MiniMessageGetParam {
    @ApiModelProperty(value = "消息模板id", required = true)
    @NotNull(message = "消息模板id不能为空")
    private Long id;
}
