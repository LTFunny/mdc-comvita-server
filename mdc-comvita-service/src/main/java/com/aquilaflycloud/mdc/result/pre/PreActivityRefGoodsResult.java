package com.aquilaflycloud.mdc.result.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PreActivityRefGoodsResult 活动关联商品result
 * @author linkq
 */
@Data
public class PreActivityRefGoodsResult {
    /**
     * 后台保存用
     */
    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    /**
     * 用于预售活动显示
     */
    @ApiModelProperty(value = "商品编号")
    private String goodsCode;

    /**
     * 用于快闪活动显示
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 用于快闪活动显示
     */
    @ApiModelProperty(value = "商品零售价")
    private BigDecimal goodsPrice;

    /**
     * 用于快闪活动显示
     */
    @ApiModelProperty(value = "商品图片")
    private String goodsPicture;
}
