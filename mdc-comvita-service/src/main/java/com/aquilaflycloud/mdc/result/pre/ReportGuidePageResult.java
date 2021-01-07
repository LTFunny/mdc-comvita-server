package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 导购员绩效
 * <p>
 * zly
 */
@Accessors(chain = true)
@Data
public class ReportGuidePageResult {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "订单编码")
    private String orderCode;

    @ApiModelProperty(value = "买家姓名")
    private String buyerName;

    @ApiModelProperty(value = "买家详细地址")
    private String buyerAddress;

    @ApiModelProperty(value = "买家地址邮编")
    private String buyerPostalCode;

    @ApiModelProperty(value = "总金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "快递名称")
    private String expressName;

    @ApiModelProperty(value = "快递单号")
    private String expressOrder;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "发货时间")
    private Date deliveryTime;

    @ApiModelProperty(value = "订单状态")
    private OrderInfoStateEnum orderState;

    @ApiModelProperty(value = "最后核销时间")
    private Date lastUpdateTime;

   /* @ApiModelProperty(value = "核销门店ids")
    private String verificaterOrgIds;

    @ApiModelProperty(value = "预约门店id")
    private String reserveShopId;

    @ApiModelProperty(value = "导购员id")
    private Long guideId;

    @ApiModelProperty(value = "核销人id")
    private Long verificaterId;
*/
    /**
     * 收货地址-省
     */
    @ApiModelProperty(value = "收货地址-省")
    private String deliveryProvince;

    /**
     * 收货地址-市
     */
    @ApiModelProperty(value = "收货地址-市")
    private String deliveryCity;

    /**
     * 收货地址-区
     */
    @ApiModelProperty(value = "收货地址-区")
    private String deliveryDistrict;

    @ApiModelProperty(value = "门店名称")
    private String shopName;

    @ApiModelProperty(value = "导购员id")
    private Long guideId;

    @ApiModelProperty(value = "导购员名称")
    private String guideName;

    @ApiModelProperty(value = "商品编号")
    private String goodsCode;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "零售价")
    private BigDecimal goodsPrice;
}
