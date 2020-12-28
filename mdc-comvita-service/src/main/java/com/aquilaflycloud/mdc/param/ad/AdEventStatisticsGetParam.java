package com.aquilaflycloud.mdc.param.ad;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * AdEventStatisticsGetParam
 *
 * @author star
 * @date 2019-11-22
 */
@Data
@Accessors(chain = true)
public class AdEventStatisticsGetParam {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "广告id不能为空")
    private Long id;

    @ApiModelProperty(value = "事件开始时间")
    private Date startTime;

    @ApiModelProperty(value = "事件结束时间")
    private Date endTime;
}


