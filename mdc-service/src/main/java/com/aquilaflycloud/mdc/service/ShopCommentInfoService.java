package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.shop.ShopCommentInfo;
import com.aquilaflycloud.mdc.param.shop.*;
import com.aquilaflycloud.mdc.result.shop.ShopCommentStarRatingAvgResult;
import com.aquilaflycloud.mdc.result.shop.ShopCommentStarRatingSortInfoResult;
import com.aquilaflycloud.mdc.result.shop.ShopStarRatingCalculateResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface ShopCommentInfoService {
    IPage<ShopCommentInfo> page(ShopCommentInfoListParam param);

    void audit(ShopCommentInfoAuditParam param);

    void delete(ShopCommentInfoDeleteParam param);

    ShopStarRatingCalculateResult getShopOperateInfo(ShopOperateInfoGetParam param);

    IPage<ShopCommentInfo> getShopOperateCommentInfoPage(ShopOperateInfoGetParam param);

    ShopCommentStarRatingSortInfoResult getStarRatingSort(ShopCommentStarRatingSortInfoParam param);

    List<ShopCommentStarRatingAvgResult> getShopInfoStarRatingAvg(List<Long> ids);

    IPage<ShopCommentInfo> getPageCommentInfo(ShopCommentInfoPageApiParam param);

    void addCommentInfo(ShopCommentInfoAddApiParam param);

    ShopCommentInfo get(ShopCommentInfoGetParam param);
}
