package com.aquilaflycloud.mdc.result.exchange;

import com.aquilaflycloud.mdc.model.catalog.CatalogInfo;
import com.aquilaflycloud.mdc.model.exchange.ExchangeGoods;
import com.aquilaflycloud.mdc.model.exchange.ExchangeGoodsSkuInfo;
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
public class GoodsResult extends ExchangeGoods {
    @ApiModelProperty(value = "兑换成功数量")
    private Integer exchangeSuccessCount;

    @ApiModelProperty(value = "优惠券信息")
    private GoodsCouponResult coupon;

    @ApiModelProperty(value = "商品图片列表")
    private List<String> goodsImgList;

    @ApiModelProperty(value = "分类列表")
    private List<CatalogInfo> catalogList;

    @ApiModelProperty(value = "单件最高金额")
    private BigDecimal singleBigPrice;

    @ApiModelProperty(value = "单件最高金额的奖励值")
    private Integer singleBigReward;

    @ApiModelProperty(value = "单件最低金额")
    private BigDecimal singleSmallPrice;

    @ApiModelProperty(value = "单件最低金额的奖励值")
    private Integer singleSmallReward;

    @ApiModelProperty(value = "商品配置的规格和规格值信息")
    private List<GoodsSpecValueInfoResult> goodsSpecValueInfoResults;

    @ApiModelProperty(value = "商品sku信息")
    private List<ExchangeGoodsSkuInfo> goodsSkuInfos;
}
