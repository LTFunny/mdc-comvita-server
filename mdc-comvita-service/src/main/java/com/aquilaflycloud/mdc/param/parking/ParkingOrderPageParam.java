package com.aquilaflycloud.mdc.param.parking;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.parking.OrderStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * ParkingOrderPageParam
 *
 * @author star
 * @date 2020-02-18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ParkingOrderPageParam extends PageParam {
    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "微信或支付宝昵称")
    private String nickName;

    @ApiModelProperty(value = "车牌号")
    private String carNo;

    @ApiModelProperty(value = "车卡号")
    private String cardId;

    @ApiModelProperty(value = "短车卡号")
    private String shortCardId;

    @ApiModelProperty(value = "订单状态(parking.OrderStateEnum)")
    private OrderStateEnum orderState;
}
