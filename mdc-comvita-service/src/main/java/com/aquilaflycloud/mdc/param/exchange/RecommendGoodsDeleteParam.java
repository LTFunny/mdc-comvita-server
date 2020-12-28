package com.aquilaflycloud.mdc.param.exchange;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * RecommendGoodsDeleteParam
 *
 * @author star
 * @date 2020-04-19
 */
@Data
public class RecommendGoodsDeleteParam implements Serializable {
    @ApiModelProperty(value = "推荐id列表", required = true)
    @NotEmpty(message = "推荐id列表不能为空")
    private List<Long> recommendIdList;
}
