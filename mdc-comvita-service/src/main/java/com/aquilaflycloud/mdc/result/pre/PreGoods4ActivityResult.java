package com.aquilaflycloud.mdc.result.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * PreGoods4ActivityResult
 * 创建活动关联商品
 * @author linkq
 */
@Data
public class PreGoods4ActivityResult {

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品编号")
    private String goodsCode;

    @ApiModelProperty(value = "商品照片")
    private String goodsPicture;

    /**
     * 类型为预售商品
     */
    @ApiModelProperty(value = "商品类型")
    private String reserveShop;

    /**
     * 状态为在售
     */
    @ApiModelProperty(value = "商品状态")
    private String reserveStartTime;

}
