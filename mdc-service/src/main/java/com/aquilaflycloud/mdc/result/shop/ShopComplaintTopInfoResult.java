package com.aquilaflycloud.mdc.result.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * SystemComplaintTopInfoResult
 *
 * @author zengqingjie
 * @date 2020-04-22
 */
@Data
public class ShopComplaintTopInfoResult {
    @ApiModelProperty(value = "店铺名称")
    private String shopFullName;

    @ApiModelProperty(value = "投诉条数")
    private Long count;
}
