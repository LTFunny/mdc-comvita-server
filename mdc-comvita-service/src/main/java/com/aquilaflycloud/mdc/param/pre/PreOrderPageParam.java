package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.ActivityTypeEnum;
import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * PreOrderPageParam
 *
 * @author star
 * @date 2021/1/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PreOrderPageParam extends PageParam<PreOrderInfo> {
    @ApiModelProperty(value = "销售门店id")
    private String shopId;

    @ApiModelProperty(value = "销售门店名称")
    private String shopName;

    @ApiModelProperty(value = "预约门店id")
    private String reserveShopId;

    @ApiModelProperty(value = "导购员名称")
    private String guideName;

    @ApiModelProperty(value = "售后导购员名称")
    private String afterGuideName;

    @ApiModelProperty(value = "订单状态")
    private String orderState;

    @ApiModelProperty(value = "关联活动")
    private String activityName;

    @ApiModelProperty(value = "关联类型")
    private String activityType;

    @ApiModelProperty(value = "订单编码")
    private String orderCode;

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "买家姓名")
    private String buyerName;

    @ApiModelProperty(value = "创建开始时间")
    private Date createStartTime;

    @ApiModelProperty(value = "创建结束时间")
    private Date createEndTime;

    @ApiModelProperty(value = "确认状态，0待确认，1已确认")
    private String confirmState;

}
