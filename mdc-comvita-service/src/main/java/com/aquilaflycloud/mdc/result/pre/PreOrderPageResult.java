package com.aquilaflycloud.mdc.result.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author zly
 */
@Data
public class PreOrderPageResult {
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品照片")
    private String goodsPicture;

    @ApiModelProperty(value = "赠品名称")
    private String giftName;

    @ApiModelProperty(value = "赠品照片")
    private String giftPicture;

    @ApiModelProperty(value = "零售价")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "商品数量")
    private int reservationNum;

    @ApiModelProperty(value = "礼包数量")
    private int giftNum;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
