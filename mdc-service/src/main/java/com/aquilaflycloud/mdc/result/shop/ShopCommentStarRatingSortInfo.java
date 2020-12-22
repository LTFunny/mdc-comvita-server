package com.aquilaflycloud.mdc.result.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ShopCommentStarRatingSortInfo implements Serializable {
    private static final long serialVersionUID = -1871223793453694453L;

    @ApiModelProperty(value="商铺名称")
    private String shopFullName;

    @ApiModelProperty(value="平均分")
    private BigDecimal average;
}
