package com.aquilaflycloud.mdc.param.exchange;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * OrderReceiveEditParam
 *
 * @author star
 * @date 2020-05-20
 */
@Data
@Accessors(chain = true)
public class OrderReceiveEditParam implements Serializable {
    @ApiModelProperty(value = "订单id", required = true)
    @NotNull(message = "订单id不能为空")
    private Long id;

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

    @ApiModelProperty(value = "订单备注")
    private String orderOperatorRemark;
}
