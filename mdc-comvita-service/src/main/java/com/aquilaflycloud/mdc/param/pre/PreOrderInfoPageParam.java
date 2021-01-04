package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author pengyongliang
 * @Date 2020/12/30 10:22
 * @Version 1.0
 */
@Data
public class PreOrderInfoPageParam extends PageParam<PreOrderInfo> implements Serializable {

    @ApiModelProperty(value = "订单状态")
    private OrderInfoStateEnum orderState;

    @ApiModelProperty(value = "会员id")
    private Long memberId;
}
