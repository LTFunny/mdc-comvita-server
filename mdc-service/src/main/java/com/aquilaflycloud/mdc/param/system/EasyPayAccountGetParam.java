package com.aquilaflycloud.mdc.param.system;

import com.aquilaflycloud.mdc.enums.system.AccountSubTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class EasyPayAccountGetParam {
    @ApiModelProperty(value = "支付账号类型", required = true)
    @NotNull(message = "支付账号类型")
    private AccountSubTypeEnum accountSubType;
}
