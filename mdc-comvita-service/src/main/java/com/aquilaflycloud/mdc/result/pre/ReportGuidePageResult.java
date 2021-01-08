package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 导购员绩效
 * <p>
 * zly
 */
@Accessors(chain = true)
@Data
public class ReportGuidePageResult {


  /*  @ApiModelProperty(value = "导购员编号")
    private Long guideId;*/

    @ApiModelProperty(value = "导购员名称")
    private String guideName;

    @ApiModelProperty(value = "拉新数量")
    private String newCustomerNum;

    @ApiModelProperty(value = "销售订单数量")
    private Integer orderNumber;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal orderPrice;
}
