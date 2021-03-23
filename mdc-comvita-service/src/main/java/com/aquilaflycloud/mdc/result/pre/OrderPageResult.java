package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * AdministrationListParam
 * <p>
 * zly
 */
@Accessors(chain = true)
@Data
public class OrderPageResult {
    @ApiModelProperty(value = "区域")
    private String deliveryDistrict;

    @ApiModelProperty(value = "省份")
    private String deliveryProvince;

    @ApiModelProperty(value = "城市")
    private String deliveryCity;

    @ApiModelProperty(value = "详细地址")
    private String deliveryAddress;

    @ApiModelProperty(value = "订单编号")
    private String orderCode;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "预约时间")
    private Date reserveStartTime;

    @ApiModelProperty(value = "发货时间")
    private Date takeTime;

    @ApiModelProperty(value = "订单状态")
    private OrderInfoStateEnum orderState;

    @ApiModelProperty(value = "买家id")
    private Long memberId;

    @ApiModelProperty(value = "买家姓名")
    private String buyerName;

    @ApiModelProperty(value = "预约人姓名")
    private String reserveName;

    @ApiModelProperty(value = "预约人手机")
    private String reservePhone;

    @ApiModelProperty(value = "销售门店名称")
    private String shopName;

    @ApiModelProperty(value = "销售员名称")
    private String guideName;

    @ApiModelProperty(value = "产品货号")
    private String goodsCode;

    @ApiModelProperty(value = "产品名称")
    private String goodsName;

    @ApiModelProperty(value = "配送卡号")
    private String cardCode;

    @ApiModelProperty(value = "销售数量")
    private Integer orderNumber;

    @ApiModelProperty(value = "零售价")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "成交价")
    private BigDecimal unitPrice;

}
