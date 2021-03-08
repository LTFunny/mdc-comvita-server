package com.aquilaflycloud.mdc.result.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RefShopInfoResult {

    @ApiModelProperty(value = "关联门店id")
    private Long shopId;

    @ApiModelProperty(value = "关联门店名称")
    private String shopName;

    @ApiModelProperty(value = "关联门店地址")
    private String shopAddress;

}
