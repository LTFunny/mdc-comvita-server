package com.aquilaflycloud.mdc.result.exchange;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * GoodsRankResult
 *
 * @author star
 * @date 2020-06-02
 */
@Data
public class GoodsRankResult implements Serializable {
    @ApiModelProperty(value = "销量排名")
    private List<GoodsSalesRank> countRank;

    @ApiModelProperty(value = "金额排名")
    private List<GoodsSalesRank> priceRank;

    @Data
    public static class GoodsSalesRank {
        @ApiModelProperty(value = "排名")
        private Long rankNo;

        @ApiModelProperty(value = "商品名称")
        private String goodsName;

        @ApiModelProperty(value = "排名数值")
        private BigDecimal value;
    }
}
