package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.mdc.enums.easypay.PayType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * OrderAddParam
 *
 * @author star
 * @date 2020-03-17
 */
@Data
public class OrderAddParam implements Serializable {
    @ApiModelProperty(value = "商品id", required = true)
    @NotNull(message = "商品id不能为空")
    private Long goodsId;

    @ApiModelProperty(value = "商品数量", required = true)
    @NotNull(message = "商品数量不能为空")
    private Integer goodsCount;

    @ApiModelProperty(value = "订单留言")
    private String orderRemark;

    @ApiModelProperty(value = "收货人姓名")
    private String receiveName;

    @ApiModelProperty(value = "收货手机")
    private String receivePhone;

    @ApiModelProperty(value = "收货地址邮编")
    private String receivePostalCode;

    @ApiModelProperty(value = "收货国家")
    private String receiveCountry;

    @ApiModelProperty(value = "收货省份")
    private String receiveProvince;

    @ApiModelProperty(value = "收货城市")
    private String receiveCity;

    @ApiModelProperty(value = "收货区域")
    private String receiveCounty;

    @ApiModelProperty(value = "收货街道")
    private String receiveStreet;

    @ApiModelProperty(value = "收货详细地址")
    private String receiveAddress;

    @ApiModelProperty(value = "支付类型(WECHAT_MINI)", required = true)
    @NotNull(message = "支付类型不能为空")
    private PayType payType;

    @ApiModelProperty(value = "下单的skuId")
    private Long skuId;
}
