package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * InputOrderNumberParam
 * <p>
 * zly
 */
@Accessors(chain = true)
@Data
public class GetOrderPageParam extends PageParam {

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "OrderInfoStateEnum订单状态")
    @NotNull(message = "订单状态不能为空")
    private OrderInfoStateEnum orderState;

    @ApiModelProperty(value = "会员id(不用传)")
    private Long memberId;

    @ApiModelProperty(value = "创建开始时间")
    private Date createStartTime;

    @ApiModelProperty(value = "创建结束时间")
    private Date createEndTime;


}
