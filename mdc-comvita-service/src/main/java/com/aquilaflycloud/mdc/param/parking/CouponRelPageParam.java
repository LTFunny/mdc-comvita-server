package com.aquilaflycloud.mdc.param.parking;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.parking.ConsumeStateEnum;
import com.aquilaflycloud.mdc.model.parking.ParkingCouponMemberRel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * CouponRelPageParam
 *
 * @author star
 * @date 2020-02-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CouponRelPageParam extends PageParam<ParkingCouponMemberRel> {
    @ApiModelProperty(value = "车牌号")
    private String carNo;

    @ApiModelProperty(value = "车卡号")
    private String cardId;

    @ApiModelProperty(value = "核销状态(parking.ConsumeStateEnum)")
    private ConsumeStateEnum consumeState;
}
