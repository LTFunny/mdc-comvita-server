package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zly
 */
@Data
public class QueryFlashCodeParam {

    @ApiModelProperty(value = "活动id")
    private Long activityInfoId;

}
