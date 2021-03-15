package com.aquilaflycloud.mdc.param.pre;

import cn.hutool.core.date.DateTime;
import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * AdministrationListParam
 * <p>
 * zly
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class ReadyListParam extends PageParam<PreOrderGoods> {
    @ApiModelProperty(value = "销售门店")
    private String reserveShop;

    @ApiModelProperty(value = "销售门店id")
    private String reserveShopId;

    @ApiModelProperty(value = "导购员名称")
    private String guideName;

    @ApiModelProperty(value = "订单编码")
    private String orderCode;

    @ApiModelProperty(value = "收件人名称")
    private String reserveName;

    @ApiModelProperty(value = "创建开始时间")
    private Date createStartTime;

    @ApiModelProperty(value = "创建结束时间")
    private Date createEndTime;

    @ApiModelProperty(value = "提交预约开始时间")
    private Date reserveStartTime;

    @ApiModelProperty(value = "提交预约结束时间")
    private Date reserveEndTime;
}
