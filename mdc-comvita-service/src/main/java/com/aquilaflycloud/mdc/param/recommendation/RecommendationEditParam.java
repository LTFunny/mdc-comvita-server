package com.aquilaflycloud.mdc.param.recommendation;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.recommendation.ReleaseTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * RecommendationEditParam
 *
 * @author star
 * @date 2020-03-27
 */
@AnotherFieldHasValue(fieldName = "releaseType", fieldValue = "REGULAR", notNullFieldName = "releaseTime", message = "发放时间不能为空")
@Data
public class RecommendationEditParam {
    @ApiModelProperty(value="最新推荐id", required = true)
    @NotNull(message = "最新推荐id不能为空")
    private Long id;

    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value="推荐标题")
    private String title;

    @ApiModelProperty(value="封面图片地址")
    private String imgUrl;

    @ApiModelProperty(value="背景图片类型")
    private String backImgType;

    @ApiModelProperty(value="发放时间")
    private Date releaseTime;

    @ApiModelProperty(value="(recommendation.ReleaseTypeEnum)")
    private ReleaseTypeEnum releaseType;

    @ApiModelProperty(value="推荐内容")
    private String content;

    @ApiModelProperty(value="是否置顶")
    private WhetherEnum isTop;

    @ApiModelProperty(value = "标签ids")
    private List<Long> folksonomyIds;

}
