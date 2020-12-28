package com.aquilaflycloud.mdc.result.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PrePickingCardAnalysisResult
 *
 * @author zengqingjie
 * @date 2020-12-28
 */
@Data
public class PrePickingCardAnalysisResult{
    @ApiModelProperty(value = "提货卡总数")
    private Long allCount;

    @ApiModelProperty(value = "已售卖提货卡")
    private Long saleCount;

    @ApiModelProperty(value = "已预约提货卡")
    private Long reserveCount;

    @ApiModelProperty(value = "已核销提货卡")
    private Long verificateCount;

    public PrePickingCardAnalysisResult() {
    }

    public void setCount(Long allCount, Long saleCount, Long reserveCount, Long verificateCount) {
        this.allCount = allCount;
        this.saleCount = saleCount;
        this.reserveCount = reserveCount;
        this.verificateCount = verificateCount;
    }
}
