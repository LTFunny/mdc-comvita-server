package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.GoodsStateStateEnum;
import com.aquilaflycloud.mdc.enums.pre.OrderGoodsTypeEnum;
import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * AdministrationListParam
 * <p>
 * zly
 */
@Accessors(chain = true)
@Data
public class AdministrationListParam extends PageParam {

    @ApiModelProperty(value = "销售门店id")
    private Long shopId;

  /*  @ApiModelProperty(value = "核销门店ids")
    private String verificaterOrgIds;*/

    @ApiModelProperty(value = "预约门店id")
    private String reserveShopId;

    @ApiModelProperty(value = "导购员id")
    private Long guideId;

   /* @ApiModelProperty(value = "核销人id")
    private Long verificaterId;*/

    @ApiModelProperty(value = "订单状态")
    private OrderInfoStateEnum orderState;

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
