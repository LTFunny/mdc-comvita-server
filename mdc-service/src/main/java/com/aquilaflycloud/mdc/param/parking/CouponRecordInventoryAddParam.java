package com.aquilaflycloud.mdc.param.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * CouponRecordInventoryAddParam
 *
 * @author star
 * @date 2020-01-14
 */
@Data
@Accessors(chain = true)
public class CouponRecordInventoryAddParam {
    @ApiModelProperty(value = "停车券id", required = true)
    @NotNull(message = "停车券名称不能为空")
    private Long couponId;

    @ApiModelProperty(value = "库存增加值", required = true)
    @NotNull(message = "停车券名称不能为空")
    @DecimalMin(value = "0.01", message = "库存增加值不能小于0.01")
    private BigDecimal inventoryIncrease;

}
