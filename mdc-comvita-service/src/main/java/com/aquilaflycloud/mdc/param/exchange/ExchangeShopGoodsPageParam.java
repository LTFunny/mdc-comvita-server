package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.exchange.ExchangeGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * ExchangeShopGoodsPageParam
 *
 * @author zengqingjie
 * @date 2020-04-16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExchangeShopGoodsPageParam extends PageParam<ExchangeGoods> implements Serializable {
    private static final long serialVersionUID = -6944056121173511584L;

    @ApiModelProperty(value = "relationId", required = true)
    @NotNull(message = "relationId不能为空")
    private Long relationId;
}
