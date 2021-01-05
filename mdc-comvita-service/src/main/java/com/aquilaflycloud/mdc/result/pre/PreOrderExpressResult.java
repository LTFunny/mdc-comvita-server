package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.enums.pre.OrderExpressActionEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zengqingjie
 * @Date 2021-01-04
 */
@Data
public class PreOrderExpressResult {

    @ApiModelProperty(value = "时间")
    private String acceptTime;

    @ApiModelProperty(value = "物流信息描述")
    private String acceptStation;

    @ApiModelProperty(value = "当前物流地点")
    private String location;

    @ApiModelProperty(value = "当前物流状态")
    private OrderExpressActionEnum action;

    public void setData(String acceptTime, String acceptStation, String location, OrderExpressActionEnum action) {
        this.acceptTime = acceptTime;
        this.acceptStation = acceptStation;
        this.location = location;
        this.action = action;
    }
}
