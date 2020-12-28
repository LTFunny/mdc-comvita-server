package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.mdc.enums.ticket.ScenicSpotTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class WechatGetProductInfoParam implements Serializable {
    @ApiModelProperty(value = "景区类型")
    private ScenicSpotTypeEnum scenicSpotType;
}
