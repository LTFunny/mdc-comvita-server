package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.exchange.RecommendTypeEnum;
import com.aquilaflycloud.mdc.model.exchange.ExchangeGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * RecommendPageParam
 *
 * @author star
 * @date 2020-04-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendPageParam extends PageParam<ExchangeGoods> implements Serializable {
    @ApiModelProperty(value = "推荐类型(exchange.RecommendTypeEnum)", required = true)
    @NotNull(message = "推荐类型不能为空")
    private RecommendTypeEnum recommendType;
}
