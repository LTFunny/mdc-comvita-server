package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.model.exchange.ExchangeRecommendRel;
import com.aquilaflycloud.mdc.result.exchange.RecommendGoodsResult;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

public interface ExchangeRecommendRelMapper extends AfcBaseMapper<ExchangeRecommendRel> {
    IPage<RecommendGoodsResult> selectPageRecommend(IPage page, @Param(Constants.WRAPPER) Wrapper wrapper);
}