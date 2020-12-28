package com.aquilaflycloud.mdc.param.isv;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * IsvGetParam
 *
 * @author star
 * @date 2019-12-17
 */
@Data
@Accessors(chain = true)
public class IsvGetParam {
    @ApiModelProperty(value = "isv应用id")
    private String appKey;
}
