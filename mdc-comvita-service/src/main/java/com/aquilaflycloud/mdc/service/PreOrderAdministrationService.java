package com.aquilaflycloud.mdc.service;


import com.aquilaflycloud.mdc.param.pre.AdministrationListParam;
import com.aquilaflycloud.mdc.param.pre.InputOrderNumberParam;
import com.aquilaflycloud.mdc.param.pre.OrderDetailsParam;
import com.aquilaflycloud.mdc.result.pre.AdministrationDetailsResult;
import com.aquilaflycloud.mdc.result.pre.AdministrationPageResult;
import com.aquilaflycloud.mdc.result.pre.RefundOrderInfoPageResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author zly
 */
public interface PreOrderAdministrationService {

    IPage<AdministrationPageResult> pageAdministrationList(AdministrationListParam param);

    IPage<RefundOrderInfoPageResult>pageOrderInfoList (AdministrationListParam param);

   void inputOrderNumber(InputOrderNumberParam param);

    AdministrationDetailsResult getOrderDetails(OrderDetailsParam param);
}
