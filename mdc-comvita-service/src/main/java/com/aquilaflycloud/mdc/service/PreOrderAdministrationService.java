package com.aquilaflycloud.mdc.service;


import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.aquilaflycloud.mdc.model.pre.PreRefundOrderInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.param.system.FileUploadParam;
import com.aquilaflycloud.mdc.result.pre.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author zly
 */
public interface PreOrderAdministrationService {

    PreOrderStatisticsResult getPreOderStatistics(PreOrderListParam param);

    IPage<PreOrderInfoResult> pagePreOder(PreOrderPageParam param);

    IPage<PreOrderInfo> pageMobilePreOder(PreOrderPageParam param);

    IPage<PreRefundOrderInfo>pageOrderInfoList (PreRefundOrderListParam param);

   void inputOrderNumber(InputOrderNumberParam param);

    AdministrationDetailsResult getOrderDetails(OrderDetailsParam param);

    AfterSalesDetailsResult getAfterOrderDetails(OrderDetailsParam param);

    IPage<PreOrderGoods> pagereadySalesList(ReadyListParam param);

    IPage<ReportOrderPageResult> pageOrderReportList(ReportFormParam param);

    IPage<ReportGuidePageResult> achievementsGuide(ReportFormParam param);

    IPage<OrderPageResult> pageOrderPageResultList(AdministrationListParam param);

    IPage<SalePageResult> pageSalePageResultList(AdministrationListParam param);

    void importOrderCode(FileUploadParam param);
}
