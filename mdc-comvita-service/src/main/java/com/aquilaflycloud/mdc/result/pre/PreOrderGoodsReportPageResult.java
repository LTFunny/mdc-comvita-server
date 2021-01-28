package com.aquilaflycloud.mdc.result.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author zly
 */
@Data
public class PreOrderGoodsReportPageResult {
    @ApiModelProperty(value = "订单编号")
    private String orderCode;

    @ApiModelProperty(value = "商品编号")
    private String goodsCode;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "配送卡号")
    private String cardCode;

    @ApiModelProperty(value = "收件人")
    private String reserveName;

    @ApiModelProperty(value = "收件地址")
    private String deliveryAddress;

    @ApiModelProperty(value = "导购员")
    private String guideName;

    @ApiModelProperty(value = "销售门店")
    private String reserveShop;

    @ApiModelProperty(value = "提交预约时间")
    private Date reserveStartTime;

    @ApiModelProperty(value = "快递编码")
    private String expressCode;

    @ApiModelProperty(value = "快递单号")
    private String expressOrderCode;

}
