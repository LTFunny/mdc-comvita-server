package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.aquilaflycloud.mdc.param.pre.ReportFormParam;
import com.aquilaflycloud.mdc.result.pre.ReportGuidePageResult;
import com.aquilaflycloud.mdc.result.pre.ReportOrderPageResult;
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
    IPage<PreOrderInfo> pageOrderInfoPageResult(IPage page, @Param("param")PreOrderInfoPageParam param);
}
