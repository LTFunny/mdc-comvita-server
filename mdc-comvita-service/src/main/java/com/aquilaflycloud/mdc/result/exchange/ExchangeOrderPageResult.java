package com.aquilaflycloud.mdc.result.exchange;

import com.aquilaflycloud.mdc.model.exchange.ExchangeOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ExchangeOrderPageResult extends ExchangeOrder {
    @ApiModelProperty(value = "优惠券信息")
    private GoodsCouponResult coupon;

    @ApiModelProperty(value = "商品图片列表")
    private List<String> goodsImgList;
}
