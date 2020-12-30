package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import com.aquilaflycloud.mdc.model.pre.PrePickingCard;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author pengyongliang
 * @Date 2020/12/30 11:29
 * @Version 1.0
 */
@Data
public class PreConfirmOrderParam {

    @ApiModelProperty(value = "订单状态" , required = true)
    @NotNull(message = "订单状态不能为空")
    private OrderInfoStateEnum orderState;

    @ApiModelProperty(value = "销售小票url")
    private String ticketUrl;

    @ApiModelProperty(value = "提货卡号")
    private List<PrePickingCard> prePickingCardList;


}
