package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.model.shop.ShopInfo;
import com.aquilaflycloud.mdc.param.shop.ShopInfoPageApiParam;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface ShopInfoMapper extends AfcBaseMapper<ShopInfo> {
    IPage<ShopInfo> selectPageInfo(IPage page, String authSql, ShopInfoPageApiParam param);
}