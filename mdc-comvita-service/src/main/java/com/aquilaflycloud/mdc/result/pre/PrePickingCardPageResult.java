package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.recommendation.Recommendation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PrePickingCardPageResult
 *
 * @author zengqingjie
 * @date 2020-12-28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PrePickingCardPageResult extends Recommendation {
    @ApiModelProperty(value = "浏览数")
    private Long readNum;
}
