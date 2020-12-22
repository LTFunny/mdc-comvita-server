package com.aquilaflycloud.mdc.result.parking;

import com.aquilaflycloud.mdc.model.parking.ParkingOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ParkingOrderApiResult extends ParkingOrder {
    @ApiModelProperty(value = "申请发票详情id")
    private Long invoiceId;
}