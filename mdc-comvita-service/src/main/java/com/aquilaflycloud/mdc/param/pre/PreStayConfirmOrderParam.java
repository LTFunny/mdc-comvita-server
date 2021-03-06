package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.mdc.enums.member.SexEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author pengyongliang
 * @Date 2020/12/29 16:40
 * @Version 1.0
 */
@Data
public class PreStayConfirmOrderParam {


    @ApiModelProperty(value = "导购员id")
    private Long guideId;

    @ApiModelProperty(value = "买家姓名")
    private String buyerName;

    @ApiModelProperty(value = "活动id")
    private Long activityInfoId;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;


    /**
     * 买家手机
     */
    @ApiModelProperty(value = "买家手机")
    private String buyerPhone;

    /**
     * 买家地址邮编
     */
    @ApiModelProperty(value = "买家地址邮编")
    private String buyerPostalCode;



    @ApiModelProperty(value = "买家地址-省")
    private String buyerProvince;

    /**
     * 买家地址-市
     */
    @ApiModelProperty(value = "买家地址-市")
    private String buyerCity;

    /**
     * 买家地址-区
     */
    @ApiModelProperty(value = "买家地址-区")
    private String buyerDistrict;

    /**
     * 买家详细地址
     */
    @ApiModelProperty(value = "买家详细地址")
    private String buyerAddress;

    /**
     * 买家生日
     */
    @ApiModelProperty(value = "买家生日")
    private String buyerBirthday;

    /**
     * 买家性别
     */
    @ApiModelProperty(value = "买家性别 0.未知 1.男 2.女")
    private SexEnum buyerSex;


    @ApiModelProperty(value = "订单id")
    private Long orderId;
}
