package com.aquilaflycloud.mdc.result.shop;

import com.aquilaflycloud.mdc.model.shop.ShopCommentInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * SystemShopOperateInfoResult
 *
 * @author zengqingjie
 * @date 2020-04-10
 */
@Data
public class ShopOperateInfoResult {
    @ApiModelProperty(value = "评论分统计结果")
    ShopStarRatingCalculateResult calculateResult;

    @ApiModelProperty(value = "评论信息分页信息")
    IPage<ShopCommentInfo> systemShopCommentInfos;

    public ShopOperateInfoResult(ShopStarRatingCalculateResult calculateResult, IPage<ShopCommentInfo> systemShopCommentInfos) {
        this.calculateResult = calculateResult;
        this.systemShopCommentInfos = systemShopCommentInfos;
    }

    public ShopOperateInfoResult() {
    }
}
