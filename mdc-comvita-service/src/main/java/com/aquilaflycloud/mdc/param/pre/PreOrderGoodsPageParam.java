package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.OrderGoodsStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @Author pengyongliang
 * @Date 2020/12/31 11:40
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PreOrderGoodsPageParam extends PageParam<PreOrderGoods> {

    @ApiModelProperty(value = "订单商品状态", required = true)
    @NotNull(message = "订单商品状态不能为空")
    private OrderGoodsStateEnum orderGoodsState;

}
