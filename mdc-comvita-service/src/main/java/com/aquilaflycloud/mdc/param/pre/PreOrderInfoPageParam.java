package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author pengyongliang
 * @Date 2020/12/30 10:22
 * @Version 1.0
 */
@Data
public class PreOrderInfoPageParam extends PageParam<PreOrderInfo> implements Serializable {

    @ApiModelProperty(value = "订单状态")
    private OrderInfoStateEnum orderState;

    @ApiModelProperty(value = "会员id(不用传)")
    private Long memberId;

    @ApiModelProperty(value = "售后标识")
    private int after;

    @ApiModelProperty(value = "买家手机")
    private String buyerPhone;

    @ApiModelProperty(value = "导购员id(不用传)")
    private Long guideId;

    @ApiModelProperty(value = "导购员标识")
    private String guideAfter;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}
