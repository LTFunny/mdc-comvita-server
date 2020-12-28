package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.exchange.ExchangeGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * ExchangeGoodsPageParam
 *
 * @author star
 * @date 2020-03-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExchangeGoodsPageParam extends PageParam<ExchangeGoods> implements Serializable {
    @ApiModelProperty(value = "分类id")
    private Long catalogId;
}
