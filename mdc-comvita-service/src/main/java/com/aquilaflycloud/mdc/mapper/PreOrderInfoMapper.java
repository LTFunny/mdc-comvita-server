package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.aquilaflycloud.mdc.param.pre.AdministrationListParam;
import com.aquilaflycloud.mdc.param.pre.GoodsInfoParam;
import com.aquilaflycloud.mdc.param.pre.ReportFormParam;
import com.aquilaflycloud.mdc.result.pre.*;
import com.aquilaflycloud.mdc.param.pre.PreOrderInfoPageParam;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

public interface PreOrderInfoMapper extends AfcBaseMapper<PreOrderInfo> {


    @InterceptorIgnore(tenantLine = "true")
    IPage<ReportOrderPageResult> pageOrderReportList(IPage page, @Param("param") ReportFormParam param);

    @InterceptorIgnore(tenantLine = "true")
    IPage<ReportGuidePageResult> achievementsGuide(IPage page, @Param("param") ReportFormParam param);

    @InterceptorIgnore(tenantLine = "true")
    IPage<OrderPageResult> pageOrderPageResultList(IPage page, @Param("param") AdministrationListParam param);

    @InterceptorIgnore(tenantLine = "true")
    IPage<SalePageResult> pageSalePageResultList(IPage page, @Param("param") AdministrationListParam param);

    @InterceptorIgnore(tenantLine = "true")
    IPage<PreOrderInfo> pageOrderInfoPageResult(IPage page, @Param("param")PreOrderInfoPageParam param);

   /* GoodsSalesVolumeResult getNum(GoodsInfoParam param);*/
}
