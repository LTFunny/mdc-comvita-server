package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.mdc.enums.pre.ActivityTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * PreActivityExportParam
 * @author linkq
 */
@Data
public class PreActivityExportParam {

    @ApiModelProperty(value = "活动类型(pre.ActivityTypeEnum)", required = true)
    @NotNull(message = "活动类型不能为空")
    private ActivityTypeEnum activityType;

}


