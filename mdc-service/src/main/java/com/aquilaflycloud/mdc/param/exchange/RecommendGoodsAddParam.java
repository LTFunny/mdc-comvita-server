package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.mdc.enums.exchange.RecommendTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * RecommendGoodsAddParam
 *
 * @author star
 * @date 2020-04-19
 */
@Data
public class RecommendGoodsAddParam implements Serializable {
    @ApiModelProperty(value = "推荐类型(exchange.RecommendTypeEnum)", required = true)
    @NotNull(message = "推荐类型不能为空")
    private RecommendTypeEnum recommendType;

    @ApiModelProperty(value = "商品id列表", required = true)
    @NotEmpty(message = "商品id列表不能为空")
    private List<Long> goodsIdList;
}
