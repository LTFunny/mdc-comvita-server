package com.aquilaflycloud.mdc.result.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 导购员绩效
 * <p>
 * zly
 */
@Accessors(chain = true)
@Data
public class GoodsSalesVolumeResult {

    @ApiModelProperty(value = "30日")
    private Integer goodsThirtyNum;

    @ApiModelProperty(value = "15日")
    private Integer goodsFifteenNum;

    @ApiModelProperty(value = "7日")
    private Integer goodsSevenNum;
}
