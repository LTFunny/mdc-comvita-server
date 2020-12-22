package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.ticket.ChannelInfoStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ChannelInfoUpdateParam extends AuthParam implements Serializable {
    private static final long serialVersionUID = 5062165695884081573L;

    @ApiModelProperty(value = "渠道id", required = true)
    @NotNull(message = "渠道id不能为空")
    private Long id;

    @ApiModelProperty(value = "状态(ticket.ChannelInfoStateEnum)", required = true)
    @NotNull(message = "状态不能为空")
    private ChannelInfoStateEnum state;
}
