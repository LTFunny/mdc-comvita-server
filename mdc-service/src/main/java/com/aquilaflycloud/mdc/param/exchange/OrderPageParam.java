package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.exchange.DeliveryTypeEnum;
import com.aquilaflycloud.mdc.enums.exchange.GoodsTypeEnum;
import com.aquilaflycloud.mdc.enums.exchange.OrderStateEnum;
import com.aquilaflycloud.mdc.enums.exchange.PayModeEnum;
import com.aquilaflycloud.mdc.model.exchange.ExchangeOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * OrderPageParam
 *
 * @author star
 * @date 2020-03-16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderPageParam extends PageAuthParam<ExchangeOrder> implements Serializable {
    @ApiModelProperty(value = "订单编号")
    private String orderCode;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "微信或支付宝昵称")
    private String nickName;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商品类型(exchange.GoodsTypeEnum)")
    private GoodsTypeEnum goodsType;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "支付方式(exchange.PayModeEnum)")
    private PayModeEnum payMode;

    @ApiModelProperty(value = "兑换开始时间")
    private Date exchangeTimeStart;

    @ApiModelProperty(value = "兑换结束时间")
    private Date exchangeTimeEnd;

    @ApiModelProperty(value = "配送方式(exchange.DeliveryTypeEnum)")
    private DeliveryTypeEnum deliveryType;

    @ApiModelProperty(value = "收货人姓名")
    private String receiveName;

    @ApiModelProperty(value = "收货手机")
    private String receivePhone;

    @ApiModelProperty(value = "支付开始时间")
    private Date payTimeStart;

    @ApiModelProperty(value = "支付结束时间")
    private Date payTimeEnd;

    @ApiModelProperty(value = "订单状态(exchange.OrderStateEnum)")
    private OrderStateEnum orderState;

    @ApiModelProperty(value = "创建开始时间")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建结束时间")
    private Date createTimeEnd;
}
