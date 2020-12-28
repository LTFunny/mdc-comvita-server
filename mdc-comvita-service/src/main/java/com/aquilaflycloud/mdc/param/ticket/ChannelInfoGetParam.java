package com.aquilaflycloud.mdc.param.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ChannelInfoGetParam implements Serializable {
    private static final long serialVersionUID = 4737098512459148154L;

    @ApiModelProperty(value = "渠道id", required = true)
    @NotNull(message = "渠道id不能为空")
    private Long id;
}
