package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author zly
 */
@Data
public class FlashConfirmOrderParam {

    @ApiModelProperty(value = "活动id")
    private Long activityInfoId;

    @ApiModelProperty(value = "导购员id")
    private Long guideId;


}
