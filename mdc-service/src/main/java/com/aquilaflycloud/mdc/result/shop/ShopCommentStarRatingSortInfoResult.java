package com.aquilaflycloud.mdc.result.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * ShopCommentStarRatingSortInfoResult
 *
 * @author zengqingjie
 * @date 2020-04-15
 */
@Data
public class ShopCommentStarRatingSortInfoResult {
    @ApiModelProperty(value = "评论平均分top10")
    List<ShopCommentStarRatingSortInfo> topInfo = new ArrayList<>();

    @ApiModelProperty(value = "评论平均分last10")
    List<ShopCommentStarRatingSortInfo> lastInfo = new ArrayList<>();

    public ShopCommentStarRatingSortInfoResult(List<ShopCommentStarRatingSortInfo> topInfo, List<ShopCommentStarRatingSortInfo> lastInfo) {
        this.topInfo = topInfo;
        this.lastInfo = lastInfo;
    }

    public ShopCommentStarRatingSortInfoResult() {
    }
}
