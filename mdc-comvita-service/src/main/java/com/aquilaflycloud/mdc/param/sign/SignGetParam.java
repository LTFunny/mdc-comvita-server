package com.aquilaflycloud.mdc.param.sign;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * SignGetParam
 *
 * @author star
 * @date 2020-05-07
 */
@Data
@Accessors(chain = true)
public class SignGetParam {
    @ApiModelProperty(value = "打卡活动id", required = true)
    @NotNull(message = "打卡活动id不能为空")
    private Long id;
}


