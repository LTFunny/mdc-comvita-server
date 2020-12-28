package com.aquilaflycloud.mdc.result.ticket;

import com.aquilaflycloud.mdc.enums.ticket.ProductInfoTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ProductInfoSaleOrderSumResult
 *
 * @author Zengqingjie
 * @date 2019-10-29
 */
@Data
public class ProductInfoSaleOrderSumResult implements Serializable {
    private static final long serialVersionUID = 2147741274894446917L;

    @ApiModelProperty(value = "日期")
    private String date;

    @ApiModelProperty(value = "景区名称")
    private String scenicSpotName;

    @ApiModelProperty(value = "产品名称")
    private String productName;

    @ApiModelProperty(value = "产品类型")
    private ProductInfoTypeEnum type;

    @ApiModelProperty(value = "订单数量")
    private Long orderCount;

    @ApiModelProperty(value = "销售总金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "购票人数")
    private Long memberCount;

    @ApiModelProperty(value = "人均消费金额")
    private Double average;

}
