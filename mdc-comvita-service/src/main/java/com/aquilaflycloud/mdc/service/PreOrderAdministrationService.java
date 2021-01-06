package com.aquilaflycloud.mdc.service;


import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.model.pre.PreRefundOrderInfo;
import com.aquilaflycloud.mdc.param.pre.AdministrationListParam;
import com.aquilaflycloud.mdc.param.pre.InputOrderNumberParam;
import com.aquilaflycloud.mdc.param.pre.OrderDetailsParam;
import com.aquilaflycloud.mdc.param.pre.ReadyListParam;
import com.aquilaflycloud.mdc.result.pre.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author zly
 */
public interface PreOrderAdministrationService {

    IPage<AdministrationPageResult> pageAdministrationList(AdministrationListParam param);

    IPage<PreRefundOrderInfo>pageOrderInfoList (AdministrationListParam param);

   void inputOrderNumber(InputOrderNumberParam param);

    AdministrationDetailsResult getOrderDetails(OrderDetailsParam param);

    AfterSalesDetailsResult getAfterOrderDetails(OrderDetailsParam param);

    IPage<PreOrderGoods> pagereadySalesList(ReadyListParam param);
}
