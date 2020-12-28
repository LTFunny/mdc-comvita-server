package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.easypay.PaymentTypeEnum;
import com.aquilaflycloud.mdc.model.easypay.EasypayPaymentRecord;
import com.aquilaflycloud.mdc.model.easypay.EasypayRefundRecord;
import com.aquilaflycloud.mdc.model.parking.ParkingOrder;
import com.aquilaflycloud.mdc.model.parking.ParkingOrderInvoice;
import com.aquilaflycloud.mdc.model.parking.ParkingUnlicensedCarRecord;
import com.aquilaflycloud.mdc.param.parking.*;
import com.aquilaflycloud.mdc.result.parking.ParkingOrderApiResult;
import com.aquilaflycloud.mdc.result.parking.ParkingOrderChargeResult;
import com.aquilaflycloud.mdc.result.parking.ParkingOrderPayResult;
import com.aquilaflycloud.mdc.result.parking.ParkingOrderResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * ParkingOrderService
 *
 * @author star
 * @date 2020-02-01
 */
public interface ParkingOrderService {
    void scanIn(UnlicensedCarParam param);

    ParkingOrderChargeResult getOrderCharge(OrderChargeGetParam param);

    List<ParkingOrderChargeResult> batchGetOrderCharge(OrderChargeBatchGetParam param);

    ParkingOrderPayResult addOrder(OrderAddParam param);

    void finishOrder(Boolean isSuccess, PaymentTypeEnum paymentType, EasypayPaymentRecord record);

    void finishRefund(Boolean isSuccess, EasypayRefundRecord record);

    IPage<ParkingOrderApiResult> pageOrder(PageParam<ParkingOrder> param);

    ParkingOrderResult getOrder(OrderGetParam param);

    void refundOrder(OrderGetParam param);

    void addInvoice(InvoiceAddParam param);

    IPage<ParkingOrderInvoice> pageInvoice(InvoicePageParam param);

    ParkingOrderInvoice getInvoice(InvoiceGetParam param);

    IPage<String> pageCarNo(CarNoPageParam param);

    IPage<ParkingUnlicensedCarRecord> pageUnlicensedCar(ParkingUnlicensedPageParam param);

    ParkingOrderChargeResult getParkingOrderCharge(ParkingOrderChargeGetParam param);

    IPage<ParkingOrder> pageParkingOrder(ParkingOrderPageParam param);

    ParkingOrderResult getParkingOrder(OrderGetParam param);

    void refundParkingOrder(ParkingOrderRefundParam param);

    IPage<ParkingOrderInvoice> pageParkingInvoice(ParkingInvoicePageParam param);
}

