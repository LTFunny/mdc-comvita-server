package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.aquilaflycloud.mdc.param.pre.AdministrationListParam;
import com.aquilaflycloud.mdc.result.pre.AdministrationPageResult;
import com.aquilaflycloud.mdc.result.pre.RefundOrderInfoPageResult;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

public interface PreOrderInfoMapper extends AfcBaseMapper<PreOrderInfo> {

    String getMaxOrderCode();

    @InterceptorIgnore(tenantLine = "true")
    IPage<AdministrationPageResult> pageAdministrationList(IPage page, @Param("param") AdministrationListParam param);

    @InterceptorIgnore(tenantLine = "true")
    IPage<RefundOrderInfoPageResult> pageOrderInfoList(IPage page, @Param("param") AdministrationListParam param);

}
