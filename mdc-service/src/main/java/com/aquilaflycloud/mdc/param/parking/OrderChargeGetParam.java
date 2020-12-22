package com.aquilaflycloud.mdc.param.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * OrderChargeGetParam
 *
 * @author star
 * @date 2020-02-01
 */
@Data
@Accessors(chain = true)
public class OrderChargeGetParam {
    @ApiModelProperty(value = "车牌号")
    private String carNo;

    @ApiModelProperty(value = "车卡号")
    private String cardId;

    @ApiModelProperty(value = "是否扫码出场(默认false)")
    private Boolean isScan = false;

    @ApiModelProperty(value = "停车券领取记录id")
    private List<Long> couponMemberRelIds;
}
