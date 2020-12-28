package com.aquilaflycloud.mdc.param.ad;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * AdGetParam
 *
 * @author star
 * @date 2019-11-18
 */
@Data
@Accessors(chain = true)
public class AdGetParam {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "广告id不能为空")
    private Long id;
}


