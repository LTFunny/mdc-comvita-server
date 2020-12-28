package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.exchange.GoodsTypeEnum;
import com.aquilaflycloud.mdc.model.exchange.ExchangeOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * ExchangeOrderPageParam
 *
 * @author star
 * @date 2020-03-25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExchangeOrderPageParam extends PageAuthParam<ExchangeOrder> implements Serializable {
    @ApiModelProperty(value = "是否实物商品")
    private Boolean isPhysical;

    @ApiModelProperty(value = "商品类型(exchange.GoodsTypeEnum)")
    private GoodsTypeEnum goodsType;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "订单状态(NOTPAY, NOTCONSUME, NOTDELIVERY, DELIVERYING, SUCCESS, REFUND)")
    private OrderStateEnum orderState;

    public enum OrderStateEnum {
        // 订单状态
        NOTPAY, NOTCONSUME, NOTDELIVERY, DELIVERYING, SUCCESS, REFUND,
    }
}
