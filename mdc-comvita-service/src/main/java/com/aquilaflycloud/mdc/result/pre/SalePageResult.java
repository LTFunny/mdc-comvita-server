package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
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
public class SalePageResult {
    @ApiModelProperty(value = "区域")
    private String deliveryDistrict;

    @ApiModelProperty(value = "省份")
    private String deliveryProvince;

    @ApiModelProperty(value = "城市")
    private String deliveryCity;

    @ApiModelProperty(value = "门店名称")
    private String shopName;

    @ApiModelProperty(value = "销售单号")
    private String orderCode;

    @ApiModelProperty(value = "促销员名称")
    private String guideName;

    @ApiModelProperty(value = "产品货号")
    private String goodsCode;

    @ApiModelProperty(value = "产品名称")
    private String goodsName;

    @ApiModelProperty(value = "销售数量")
    private Integer orderNumber;

    @ApiModelProperty(value = "零售价")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "销售单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "成交总额")
    private BigDecimal totaGoodsPrice;
}
