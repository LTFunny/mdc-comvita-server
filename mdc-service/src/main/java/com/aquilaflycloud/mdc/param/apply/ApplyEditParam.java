package com.aquilaflycloud.mdc.param.apply;

import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.result.apply.ApplyRule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * ApplyEditParam
 *
 * @author star
 * @date 2020-02-27
 */
@Data
@Accessors(chain = true)
public class ApplyEditParam {
    @ApiModelProperty(value = "报名活动id", required = true)
    @NotNull(message = "报名活动id不能为空")
    private Long id;

    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value = "报名活动名称")
    private String applyName;

    @ApiModelProperty(value = "活动开始时间")
    private Date startTime;

    @ApiModelProperty(value = "活动结束时间")
    private Date endTime;

    @ApiModelProperty(value = "活动省份")
    private String province;

    @ApiModelProperty(value = "活动城市")
    private String city;

    @ApiModelProperty(value = "活动区域")
    private String county;

    @ApiModelProperty(value = "省-市-区名称")
    private String address;

    @ApiModelProperty(value = "活动地址名称")
    private String placeName;

    @ApiModelProperty(value = "地址经度")
    private String longitude;

    @ApiModelProperty(value = "地址维度")
    private String latitude;

    @ApiModelProperty(value = "活动图片地址")
    private String imageUrl;

    @ApiModelProperty(value = "报名人数上限(0表示无上限)")
    private Integer applyLimit;

    @ApiModelProperty(value = "是否置顶")
    private WhetherEnum isTop;

    @ApiModelProperty(value = "报名规则")
    @Valid
    private ApplyRule applyRule;

    @ApiModelProperty(value = "活动介绍")
    private String introduction;

    @ApiModelProperty(value = "标签ids")
    private List<Long> folksonomyIds;
}


