package com.aquilaflycloud.mdc.result.exchange;

import com.aquilaflycloud.mdc.model.exchange.ExchangeGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * GoodsResult
 *
 * @author star
 * @date 2020-03-15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExchangeGoodsResult extends ExchangeGoods {
    @ApiModelProperty(value = "优惠券信息")
    private GoodsCouponResult coupon;

    @ApiModelProperty(value = "商品图片列表")
    private List<String> goodsImgList;

    @ApiModelProperty(value = "单件最低金额")
    private BigDecimal singleSmallPrice;

    @ApiModelProperty(value = "单件最低金额的奖励值")
    private Integer singleSmallReward;
}
