package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.mdc.enums.pre.ActivityStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * PreActivityCancelParam
 * 下架
 * @author linkq
 */
@Data
public class PreActivityCancelParam {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "活动状态(pre.ActivityStateEnum)")
    private ActivityStateEnum activityState;

}
