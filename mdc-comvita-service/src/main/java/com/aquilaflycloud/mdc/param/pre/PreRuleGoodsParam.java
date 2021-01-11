package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PreRuleGoodsParam
 * @author linkq
 */
@Data
public class PreRuleGoodsParam {

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String goodsCode;

}
