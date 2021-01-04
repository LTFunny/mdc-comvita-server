package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreOrderExpress;
import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author pengyongliang
 * @Date 2021/1/4 9:56
 * @Version 1.0
 */
@Data
public class PreOrderInfoPageResult extends PreOrderInfo {

    @ApiModelProperty(value = "商品信息")
    private List<PreOrderGoods> preOrderGoodsList;

    @ApiModelProperty(value = "状态标识")
    private OrderInfoStateEnum identificationState;

    @ApiModelProperty(value = "预约数量")
    private int reservationNum;

    @ApiModelProperty(value = "提货数量")
    private int ingdeliveryNum;

    @ApiModelProperty(value = "快递信息")
    private PreOrderExpress preOrderExpress;
}
