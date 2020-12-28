package com.aquilaflycloud.mdc.param.recommendation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * RecommendationGetParam
 *
 * @author star
 * @date 2020-03-27
 */
@Data
@Accessors(chain = true)
public class RecommendationGetParam implements Serializable {
    @ApiModelProperty(value = "最新推荐id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;
}
