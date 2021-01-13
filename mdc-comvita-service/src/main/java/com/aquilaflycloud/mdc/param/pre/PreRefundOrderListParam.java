package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.pre.PreRefundOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * PreRefundOrderListParam
 *
 * @author star
 * @date 2021/1/13
 */
@Data
public class PreRefundOrderListParam extends PageParam<PreRefundOrderInfo> {

    @ApiModelProperty(value = "销售门店id")
    private String shopId;

  /*  @ApiModelProperty(value = "核销门店ids")
    private String verificaterOrgIds;*/

    @ApiModelProperty(value = "预约门店id")
    private String reserveShopId;

    @ApiModelProperty(value = "导购员名称")
    private String guideName;

    @ApiModelProperty(value = "售后导购员名称")
    private String afterGuideName;
   /* @ApiModelProperty(value = "核销人id")
    private Long verificaterId;*/

    @ApiModelProperty(value = "订单状态")
    private String orderState;

    @ApiModelProperty(value = "订单编码")
    private String orderCode;

    @ApiModelProperty(value = "买家姓名")
    private String buyerName;

    @ApiModelProperty(value = "创建开始时间")
    private Date createStartTime;

    @ApiModelProperty(value = "创建结束时间")
    private Date createEndTime;
/*
    @ApiModelProperty(value = "核销开始时间")
    private Date writeOffStartTime;

    @ApiModelProperty(value = "核销结束时间")
    private Date writeOffEndTime;*/

    @ApiModelProperty(value = "发货开始时间")
    private Date sendStartTime;

    @ApiModelProperty(value = "发货结束时间")
    private Date sendEndTime;

    @ApiModelProperty(value = "售后开始时间")
    private Date afterSalesStartTime;

    @ApiModelProperty(value = "售后结束时间")
    private Date afterSalEndTime;
}
