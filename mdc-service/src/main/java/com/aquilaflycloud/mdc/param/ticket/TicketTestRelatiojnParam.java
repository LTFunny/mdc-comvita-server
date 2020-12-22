package com.aquilaflycloud.mdc.param.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class TicketTestRelatiojnParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关系id", required = true)
    @NotNull(message = "关系id不能为空")
    private Long id;
}
