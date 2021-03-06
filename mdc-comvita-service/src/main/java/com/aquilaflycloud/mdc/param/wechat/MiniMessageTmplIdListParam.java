package com.aquilaflycloud.mdc.param.wechat;

import com.aquilaflycloud.mdc.enums.wechat.MiniMessageTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Accessors(chain = true)
public class MiniMessageTmplIdListParam {
    @ApiModelProperty(value = "消息类型列表(wechat.MiniMessageTypeEnum)")
    @NotEmpty(message = "消息类型列表不能为空")
    private List<MiniMessageTypeEnum> messageTypeList;
}
