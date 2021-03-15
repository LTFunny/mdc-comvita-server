package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.ActivityTypeEnum;
import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PreOrderInfoPageApiParam
 *
 * @author star
 * @date 2021/3/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PreOrderInfoPageApiParam extends PageParam<PreOrderInfo> {
    @ApiModelProperty(value = "关联活动类型(pre.ActivityTypeEnum)")
    private ActivityTypeEnum activityType;

    @ApiModelProperty(value = "订单状态(pre.OrderInfoStateEnum)")
    private OrderInfoStateEnum orderState;

    @ApiModelProperty(value = "售后标识")
    private int after;
}
