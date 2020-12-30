package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author pengyongliang
 * @Date 2020/12/30 10:22
 * @Version 1.0
 */
@Data
public class PreOrderInfoPageParam extends PageAuthParam<PreOrderInfo> implements Serializable {

    @ApiModelProperty(value = "订单状态")
    private OrderInfoStateEnum orderState;

    @ApiModelProperty(value = "导购员id", required = true)
    @NotNull(message = "导购员id")
    private Long guideId;
}
