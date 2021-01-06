package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.OrderGoodsStateEnum;
import com.aquilaflycloud.mdc.enums.pre.PickingCardStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author pengyongliang
 * @Date 2020/12/31 11:40
 * @Version 1.0
 */
@Data
public class PreOrderGoodsPageParam extends PageParam {

    @ApiModelProperty(value = "会员id",required = true)
    @NotNull(message = "会员id不能为空")
    private Long memberId;

    @ApiModelProperty(value = "订单商品状态", required = true)
    @NotNull(message = "订单商品状态不能为空")
    private OrderGoodsStateEnum orderGoodsState;

}
