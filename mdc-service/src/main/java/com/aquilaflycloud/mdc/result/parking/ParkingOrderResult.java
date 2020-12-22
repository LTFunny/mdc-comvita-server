package com.aquilaflycloud.mdc.result.parking;

import com.aquilaflycloud.mdc.model.easypay.EasypayPaymentRecord;
import com.aquilaflycloud.mdc.model.easypay.EasypayRefundRecord;
import com.aquilaflycloud.mdc.model.parking.ParkingCouponMemberRel;
import com.aquilaflycloud.mdc.model.parking.ParkingOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ParkingOrderResult extends ParkingOrder {
    @ApiModelProperty(value = "停车券列表")
    private List<ParkingCouponMemberRel> couponRelList;

    @ApiModelProperty(value = "支付记录")
    private List<EasypayPaymentRecord> paymentRecordList;

    @ApiModelProperty(value = "退款记录")
    private List<EasypayRefundRecord> refundRecordList;
}