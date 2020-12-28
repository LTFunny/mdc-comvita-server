package com.aquilaflycloud.mdc.param.parking;

import com.aquilaflycloud.common.NoneBlank;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * OrderChargeInfoGetParam
 *
 * @author star
 * @date 2020-02-17
 */
@NoneBlank(fieldNames = {"carNo", "cardId", "shortCardId"}, message = "车牌号,车卡号,短车卡号不能同时为空")
@Data
@Accessors(chain = true)
public class ParkingOrderChargeGetParam {
    @ApiModelProperty(value = "车牌号")
    private String carNo;

    @ApiModelProperty(value = "车卡号")
    private String cardId;

    @ApiModelProperty(value = "短车卡号")
    private String shortCardId;

    @ApiModelProperty(value = "停车券id")
    private Long parkingCouponId;

    @ApiModelProperty(value = "派发金额")
    @DecimalMin(value = "0.01", message = "派发金额不能小于0.01")
    private BigDecimal value;

    @ApiModelProperty(value = "派发数")
    @Min(value = 1, message = "派发数不能小于1")
    private Integer count;
}
