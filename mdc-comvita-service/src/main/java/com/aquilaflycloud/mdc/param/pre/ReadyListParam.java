package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
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
public class ReadyListParam extends PageParam {
    @ApiModelProperty(value = "销售门店")
    private String reserveShop;

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
}
