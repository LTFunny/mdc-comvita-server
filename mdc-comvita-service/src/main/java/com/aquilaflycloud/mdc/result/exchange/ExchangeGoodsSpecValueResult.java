package com.aquilaflycloud.mdc.result.exchange;

import com.aquilaflycloud.mdc.model.exchange.ExchangeGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * ExchangeGoodsSpecValueResult
 *
 * @author zengqingjie
 * @date 2020-07-06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExchangeGoodsSpecValueResult extends ExchangeGoods {
    @ApiModelProperty(value = "规格和规格值的配置信息")
    private List<GoodsSpecValueInfoResult> goodsSpecValueInfoResults;
}
