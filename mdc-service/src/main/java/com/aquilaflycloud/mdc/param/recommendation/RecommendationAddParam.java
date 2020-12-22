package com.aquilaflycloud.mdc.param.recommendation;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.constant.MdcConstant;
import com.aquilaflycloud.mdc.enums.recommendation.ReleaseTypeEnum;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * RecommendationAddParam
 *
 * @author star
 * @date 2020-03-27
 */
@AnotherFieldHasValue(fieldName = "releaseType", fieldValue = "REGULAR", notNullFieldName = "releaseTime", message = "发放时间不能为空")
@Accessors(chain = true)
@Data
public class RecommendationAddParam {
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId = MdcConstant.UNIVERSAL_APP_ID;

    @ApiModelProperty(value = "推荐标题", required = true)
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty(value = "封面图片地址", required = true)
    @NotBlank(message = "封面图片不能为空")
    private String imgUrl;

    @ApiModelProperty(value = "背景图片类型", required = true)
    @NotBlank(message = "背景图片不能为空")
    private String backImgType;

    @ApiModelProperty(value = "发放时间")
    private Date releaseTime;

    @ApiModelProperty(value = "发放类型(recommendation.ReleaseTypeEnum)")
    private ReleaseTypeEnum releaseType;

    @ApiModelProperty(value = "推荐内容", required = true)
    @NotBlank(message = "推荐内容不能为空")
    private String content;

    @ApiModelProperty(value = "是否置顶(common.WhetherEnum)(默认不置顶)")
    private WhetherEnum isTop = WhetherEnum.NO;

    @ApiModelProperty(value = "标签ids")
    private List<Long> folksonomyIds;
}
