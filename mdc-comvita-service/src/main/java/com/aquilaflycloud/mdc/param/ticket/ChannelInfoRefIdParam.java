package com.aquilaflycloud.mdc.param.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ChannelInfoRefIdParam implements Serializable {
    @ApiModelProperty(value = "渠道id", required = true)
    @NotNull(message = "渠道id不能为空")
    private Long id;
}
