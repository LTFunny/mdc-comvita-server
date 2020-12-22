package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * UserAnalysisSumGetParam
 *
 * @author star
 * @date 2019-11-13
 */
@Data
@Accessors(chain = true)
public class UserAnalysisSumGetParam {

    @ApiModelProperty(value = "公众号appId")
    private String appId;

    @ApiModelProperty(value = "开始时间")
    private String beginDate;

    @ApiModelProperty(value = "结束时间")
    private String endDate;
}
