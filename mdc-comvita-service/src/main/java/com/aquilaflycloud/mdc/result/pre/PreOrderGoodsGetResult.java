package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.enums.member.SexEnum;
import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author pengyongliang
 * @Date 2021/1/5 11:28
 * @Version 1.0
 */
@Data
public class PreOrderGoodsGetResult {

    @ApiModelProperty(value = "商品信息")
    private PreGoodsInfo goodsInfo;

    @ApiModelProperty(value = "订单信息")
    private PreOrderInfo orderInfo;

    @ApiModelProperty(value = "预约人id(会员id)")
    private Long reserveId;

    /**
     * 预约人名称
     */
    @ApiModelProperty(value = "预约人名称")
    private String reserveName;

    /**
     * 预约人电话
     */
    @ApiModelProperty(value = "预约人电话")
    private String reservePhone;

    /**
     * 预约门店
     */
    @ApiModelProperty(value = "预约门店")
    private String reserveShop;

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

    @ApiModelProperty(value = "性别")
    private SexEnum sex;

    @ApiModelProperty(value = "出生日期")
    private Date birthday;

}
