package com.aquilaflycloud.mdc.result.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ShopStarRatingCalculateResult implements Serializable {
    private static final long serialVersionUID = -249558903709406560L;

    @ApiModelProperty(value = "平均评论分")
    private double average = 0;

    @ApiModelProperty(value = "好评数")
    private Long goodCount = 0L;

    @ApiModelProperty(value = "差评数")
    private Long badCount = 0L;
}
