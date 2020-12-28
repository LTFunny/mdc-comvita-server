package com.aquilaflycloud.mdc.param.exchange;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * RecommendGoodsEditParam
 *
 * @author star
 * @date 2020-04-19
 */
@Data
public class RecommendGoodsEditParam implements Serializable {
    @ApiModelProperty(value = "推荐排序列表", required = true)
    @NotEmpty(message = "推荐排序列表不能为空")
    @Valid
    private List<RecommendRel> recommendRelList;

    @Data
    public class RecommendRel {
        @ApiModelProperty(value = "推荐id", required = true)
        @NotNull(message = "推荐id不能为空")
        private Long recommendId;

        @ApiModelProperty(value = "排序", required = true)
        @NotNull(message = "排序不能为空")
        private Integer recommendOrder;
    }
}
