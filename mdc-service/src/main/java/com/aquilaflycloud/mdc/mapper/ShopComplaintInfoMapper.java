package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.model.shop.ShopComplaintInfo;
import com.aquilaflycloud.mdc.result.shop.ShopComplaintTopInfoResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface ShopComplaintInfoMapper extends AfcBaseMapper<ShopComplaintInfo> {

    List<ShopComplaintTopInfoResult> statistics(String authSql);
}
