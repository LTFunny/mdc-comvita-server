package com.aquilaflycloud.mdc.result.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * AuthorUniversalConfig
 *
 * @author star
 * @date 2020-04-28
 */
@Data
public class AuthorUniversalConfig {
    @ApiModelProperty(value = "是否显示通用应用")
    private Boolean visible;
}
