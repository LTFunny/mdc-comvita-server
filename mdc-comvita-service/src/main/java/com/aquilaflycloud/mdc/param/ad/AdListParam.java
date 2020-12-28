package com.aquilaflycloud.mdc.param.ad;

import com.aquilaflycloud.mdc.enums.ad.AdPlacementEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * AdListParam
 *
 * @author star
 * @date 2020-03-26
 */
@Data
@Accessors(chain = true)
public class AdListParam {
    @ApiModelProperty(value = "投放位置(ad.AdPlacementEnum)", required = true)
    @NotNull(message = "投放位置不能为空")
    private AdPlacementEnum adPlacement;

    @ApiModelProperty(value = "获取广告数量(默认3个)")
    private Integer limit = 3;
}


