package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.model.shop.ShopCommentInfo;
import com.aquilaflycloud.mdc.result.shop.ShopCommentStarRatingAvgResult;
import com.aquilaflycloud.mdc.result.shop.ShopCommentStarRatingSortInfo;

import java.util.List;

public interface ShopCommentInfoMapper extends AfcBaseMapper<ShopCommentInfo> {

    List<ShopCommentStarRatingSortInfo> getStarRatingTopInfo(String authSql);

    List<ShopCommentStarRatingSortInfo> getStarRatingLastInfo(String authSql);

    List<ShopCommentStarRatingAvgResult> getShopInfoStarRatingAvg(List<Long> ids);
}
