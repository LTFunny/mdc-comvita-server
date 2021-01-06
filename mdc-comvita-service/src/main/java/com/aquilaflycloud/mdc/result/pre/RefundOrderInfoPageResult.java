package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * RefundOrderInfoPageResult
 * <p>售后订单
 * zly
 */
@Accessors(chain = true)
@Data
public class RefundOrderInfoPageResult {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "售后单编码")
    private String refundCode;

    @ApiModelProperty(value = "订单编码")
    private String orderCode;

    @ApiModelProperty(value = "买家姓名")
    private String buyerName;

    @ApiModelProperty(value = "总金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundPrice;

    @ApiModelProperty(value = "售后商品状态")
    private Integer refundGoodsState;

    @ApiModelProperty(value = "售后原因")
    private String refundReason;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "售后时间")
    private Date refundTime;
}
