package com.aquilaflycloud.mdc.param.ad;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * AdShowParam
 *
 * @author star
 * @date 2019-11-20
 */
@Data
@Accessors(chain = true)
public class AdShowParam {
    @ApiModelProperty(value = "获取广告数量(默认3个)")
    private Integer limit = 3;
}


