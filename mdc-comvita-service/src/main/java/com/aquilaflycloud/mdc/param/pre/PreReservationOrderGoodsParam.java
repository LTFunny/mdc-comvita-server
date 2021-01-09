package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.mdc.enums.pre.IsUpdateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author pengyongliang
 * @Date 2020/12/30 16:51
 * @Version 1.0
 */
@Data
public class PreReservationOrderGoodsParam  {

    @ApiModelProperty(value = "订单商品id")
    private Long orderGoodsId;

    @ApiModelProperty(value = "提货卡号密码")
    private String password;

    @ApiModelProperty(value = "预约人名称")
    private String reserveName;

    @ApiModelProperty(value = "预约人电话")
    private String reservePhone;

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

    /**
     * 收货地址-详细地址
     */
    @ApiModelProperty(value = "收货地址-详细地址")
    private String deliveryAddress;

    /**
     * 预约开始时间
     */
    @ApiModelProperty(value = "预约开始时间")
    private Date reserveStartTime;

    /**
     * 预约结束时间
     */
    @ApiModelProperty(value = "预约结束时间")
    private Date reserveEndTime;

    @ApiModelProperty(value = "是否可修改")
    private IsUpdateEnum isUpdate;

}
