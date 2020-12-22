package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.exchange.GoodsStateEnum;
import com.aquilaflycloud.mdc.enums.exchange.GoodsTypeEnum;
import com.aquilaflycloud.mdc.enums.exchange.RecommendTypeEnum;
import com.aquilaflycloud.mdc.model.exchange.ExchangeGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * RecommendGoodsPageParam
 *
 * @author star
 * @date 2020-04-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendGoodsPageParam extends PageAuthParam<ExchangeGoods> implements Serializable {
    @ApiModelProperty(value = "推荐类型(exchange.RecommendTypeEnum)", required = true)
    @NotNull(message = "推荐类型不能为空")
    private RecommendTypeEnum recommendType;

    @ApiModelProperty(value = "商品类型(exchange.GoodsTypeEnum)")
    private GoodsTypeEnum goodsType;

    @ApiModelProperty(value = "商品编码")
    private String goodsCode;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "上架开始时间")
    private Date shelveTimeStart;

    @ApiModelProperty(value = "上架结束时间")
    private Date shelveTimeEnd;

    @ApiModelProperty(value = "状态(exchange.GoodsStateEnum)")
    private GoodsStateEnum state;
}
