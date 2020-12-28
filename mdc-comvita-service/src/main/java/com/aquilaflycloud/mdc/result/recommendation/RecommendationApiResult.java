package com.aquilaflycloud.mdc.result.recommendation;

import com.aquilaflycloud.mdc.model.recommendation.Recommendation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * RecommendationResult
 *
 * @author star
 * @date 2019-12-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendationApiResult extends Recommendation {
    @ApiModelProperty(value = "浏览数")
    private Long readNum;
}
