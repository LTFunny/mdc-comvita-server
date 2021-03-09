package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author zly
 */
@Data
public class FlashConfirmOrderParam {

    @ApiModelProperty(value = "活动id", required = true)
    @NotNull(message = "活动id不能为空")
    private Long activityInfoId;

    @ApiModelProperty(value = "门店id", required = true)
    @NotNull(message = "门店id不能为空")
    private Long shopId;

    @ApiModelProperty(value = "门店名称", required = true)
    @NotNull(message = "门店名称不能为空")
    private String shopName;
    /**
     * 买家手机
     */
    @ApiModelProperty(value = "买家姓名(线上发货填写)")
    private String buyerName;

    /**
     * 买家手机
     */
    @ApiModelProperty(value = "买家手机(线上发货填写)")
    private String buyerPhone;

    /**
     * 买家地址邮编
     */
    @ApiModelProperty(value = "买家地址邮编(线上发货填写)")
    private String buyerPostalCode;



    @ApiModelProperty(value = "买家地址-省(线上发货填写)")
    private String buyerProvince;

    /**
     * 买家地址-市
     */
    @ApiModelProperty(value = "买家地址-市(线上发货填写)")
    private String buyerCity;

    /**
     * 买家地址-区
     */
    @ApiModelProperty(value = "买家地址-区(线上发货填写)")
    private String buyerDistrict;

    /**
     * 买家详细地址
     */
    @ApiModelProperty(value = "买家详细地址(线上发货填写)")
    private String buyerAddress;
}
