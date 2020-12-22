package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.ticket.ScenicSpotTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ScenicSpotInfoGetParam extends AuthParam implements Serializable {
    private static final long serialVersionUID = -4074231828145080894L;

    @ApiModelProperty(value = "景区类型(ticket.ScenicSpotTypeEnum)", required = true)
    @NotNull(message = "景区类型不能为空")
    private ScenicSpotTypeEnum type;
}
