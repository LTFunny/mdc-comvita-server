package com.aquilaflycloud.mdc.result.exchange;

import com.aquilaflycloud.mdc.enums.exchange.RecommendTypeEnum;
import com.aquilaflycloud.mdc.model.exchange.ExchangeGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * RecommendGoodsResult
 *
 * @author star
 * @date 2020-04-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendGoodsResult extends ExchangeGoods {
    @ApiModelProperty(value = "推荐id")
    private Long recommendId;

    @ApiModelProperty(value = "推荐类型")
    private RecommendTypeEnum recommendType;

    @ApiModelProperty(value = "排序")
    private Integer recommendOrder;

    @ApiModelProperty(value = "单件最高金额")
    private BigDecimal singleBigPrice;

    @ApiModelProperty(value = "单件最高金额的奖励值")
    private Integer singleBigReward;

    @ApiModelProperty(value = "单件最低金额")
    private BigDecimal singleSmallPrice;

    @ApiModelProperty(value = "单件最低金额的奖励值")
    private Integer singleSmallReward;
}
