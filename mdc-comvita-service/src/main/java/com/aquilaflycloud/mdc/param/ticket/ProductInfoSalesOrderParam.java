package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.ticket.ProductInfoAllTypeEnum;
import com.aquilaflycloud.mdc.model.ticket.TicketProductInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 产品售卖分析
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ProductInfoSalesOrderParam extends PageAuthParam<TicketProductInfo> implements Serializable {

    private static final long serialVersionUID = 8854946962849257822L;

    @ApiModelProperty(value = "门票类型(ticket.ProductInfoAllTypeEnum)")
    private ProductInfoAllTypeEnum productType;

    @ApiModelProperty(value = "开始时间", required = true)
    @NotNull(message = "开始时间不能为空")
    private Date startDate;

    @ApiModelProperty(value = "结束时间", required = true)
    @NotNull(message = "结束时间不能为空")
    private Date endDate;
}
