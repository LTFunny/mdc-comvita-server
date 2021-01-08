package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.enums.pre.OrderInfoListStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import com.aquilaflycloud.mdc.model.pre.PreOrderExpress;
import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author pengyongliang
 * @Date 2021/1/4 9:56
 * @Version 1.0
 */
@Data
public class PreOrderInfoPageResult extends PreOrderInfo {

    @ApiModelProperty(value = "商品信息")
    private PreGoodsInfo preGoodsInfo;

    @ApiModelProperty(value = "商品信息数量")
    private int goodsInfoNum;

    @ApiModelProperty(value = "赠品信息")
    private PreOrderGoods giftsGoodsInfo;

    @ApiModelProperty(value = "状态标识")
    private OrderInfoListStateEnum orderInfoListState;

    @ApiModelProperty(value = "预约数量")
    private int reservationNum;

    @ApiModelProperty(value = "提货数量")
    private int ingdeliveryNum;

    @ApiModelProperty(value = "快递信息")
    private PreOrderExpress preOrderExpress;

    @ApiModelProperty(value = "订单状态(state)")
    private String state;

}
