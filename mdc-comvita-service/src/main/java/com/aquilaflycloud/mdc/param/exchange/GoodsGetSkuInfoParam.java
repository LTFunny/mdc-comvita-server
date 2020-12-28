package com.aquilaflycloud.mdc.param.exchange;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * GoodsGetSkuInfoParam
 *
 * @author zengqingjie
 * @date 2020-07-06
 */
@Data
public class GoodsGetSkuInfoParam implements Serializable {
    @ApiModelProperty(value = "商品id", required = true)
    @NotNull(message = "商品id不能为空")
    private Long id;

    @ApiModelProperty(value = "规格ids", required = true)
    @NotNull(message = "规格信息不能为空")
    private String specIds;

    @ApiModelProperty(value = "规格值ids", required = true)
    @NotNull(message = "规格值信息不能为空")
    private String specValueIds;
}
