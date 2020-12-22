package com.aquilaflycloud.mdc.result.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ShopCommentStarRatingAvgResult
 *
 * @author zengqingjie
 * @date 2020-04-15
 */
@Data
public class ShopCommentStarRatingAvgResult implements Serializable {
    private static final long serialVersionUID = 8623456304573821558L;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "评分")
    BigDecimal average;
}
