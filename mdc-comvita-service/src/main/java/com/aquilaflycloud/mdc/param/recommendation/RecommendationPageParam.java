package com.aquilaflycloud.mdc.param.recommendation;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.recommendation.RecommendationStateEnum;
import com.aquilaflycloud.mdc.model.recommendation.Recommendation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * RecommendationPageParam
 *
 * @author star
 * @date 2020-03-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendationPageParam extends PageAuthParam<Recommendation> {
    @ApiModelProperty(value="微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value="标题")
    private String title;

    @ApiModelProperty(value="创建时间开始")
    private Date createTimeStart;

    @ApiModelProperty(value="创建时间结束")
    private Date createTimeEnd;

    @ApiModelProperty(value="状态(recommendation.RecommendationStateEnum)")
    private RecommendationStateEnum state;

    @ApiModelProperty(value = "是否查询有效状态(common.WhetherEnum)(默认否, 为是时state无效)")
    private WhetherEnum valid = WhetherEnum.NO;
}
