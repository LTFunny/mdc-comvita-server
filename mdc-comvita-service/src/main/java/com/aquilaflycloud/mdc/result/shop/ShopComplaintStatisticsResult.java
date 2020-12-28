package com.aquilaflycloud.mdc.result.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * SystemComplaintStatisticsResult
 *
 * @author zengqingjie
 * @date 2020-04-22
 */
@Data
public class ShopComplaintStatisticsResult implements Serializable {
    private static final long serialVersionUID = -3877352106665808849L;
    @ApiModelProperty(value = "店铺名称")
    private List<ShopComplaintTopInfoResult> topInfo;

    @ApiModelProperty(value = "待处理投诉数量")
    private Long acceptCount;

    @ApiModelProperty(value = "已通过投诉数量")
    private Long passCount;

    @ApiModelProperty(value = "申诉成功投诉数量")
    private Long appealSuccessCount;

    public ShopComplaintStatisticsResult() {
    }

    public ShopComplaintStatisticsResult(List<ShopComplaintTopInfoResult> topInfo, Long acceptCount, Long passCount, Long appealSuccessCount) {
        this.topInfo = topInfo;
        this.acceptCount = acceptCount;
        this.passCount = passCount;
        this.appealSuccessCount = appealSuccessCount;
    }
}
