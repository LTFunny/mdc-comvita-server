package com.aquilaflycloud.mdc.result.recommendation;

import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.recommendation.Recommendation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * RecommendationResult
 *
 * @author star
 * @date 2019-12-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendationResult extends Recommendation {
    @ApiModelProperty(value = "标签信息列表")
    List<FolksonomyInfo> folksonomyInfoList;
}
