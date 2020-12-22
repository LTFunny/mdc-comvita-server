package com.aquilaflycloud.mdc.param.wechat;

import com.aquilaflycloud.mdc.enums.wechat.MiniMessageTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class MiniMessageTmplIdListParam {
    @ApiModelProperty(value = "消息类型(wechat.MiniMessageTypeEnum)")
    @NotNull(message = "消息类型不能为空")
    private MiniMessageTypeEnum messageType;
}
