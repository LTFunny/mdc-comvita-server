package com.aquilaflycloud.mdc.param.apply;

import com.aquilaflycloud.mdc.constant.MdcConstant;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.result.apply.ApplyRule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * ApplyAddParam
 *
 * @author star
 * @date 2020-02-27
 */
@Data
@Accessors(chain = true)
public class ApplyAddParam {
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId = MdcConstant.UNIVERSAL_APP_ID;

    @ApiModelProperty(value = "报名活动名称", required = true)
    @NotBlank(message = "报名活动名称不能为空")
    private String applyName;

    @ApiModelProperty(value = "活动开始时间", required = true)
    @NotNull(message = "报名开始时间不能为空")
    private Date startTime;

    @ApiModelProperty(value = "活动结束时间", required = true)
    @NotNull(message = "活动结束时间不能为空")
    private Date endTime;

    @ApiModelProperty(value = "活动省份")
    private String province;

    @ApiModelProperty(value = "活动城市")
    private String city;

    @ApiModelProperty(value = "活动区域")
    private String county;

    @ApiModelProperty(value = "省-市-区名称", required = true)
    @NotBlank(message = "省-市-区名称不能为空")
    private String address;

    @ApiModelProperty(value = "活动地址名称", required = true)
    @NotBlank(message = "活动地址名称不能为空")
    private String placeName;

    @ApiModelProperty(value = "地址经度", required = true)
    @NotBlank(message = "地址经度不能为空")
    private String longitude;

    @ApiModelProperty(value = "地址维度", required = true)
    @NotBlank(message = "地址维度不能为空")
    private String latitude;

    @ApiModelProperty(value = "活动图片地址", required = true)
    @NotBlank(message = "活动图片地址不能为空")
    private String imageUrl;

    @ApiModelProperty(value = "报名人数上限(0表示无上限)")
    private Integer applyLimit = 0;

    @ApiModelProperty(value = "是否置顶")
    private WhetherEnum isTop = WhetherEnum.NO;

    @ApiModelProperty(value = "报名规则")
    @Valid
    private ApplyRule applyRule;

    @ApiModelProperty(value = "活动介绍", required = true)
    @NotBlank(message = "活动介绍不能为空")
    private String introduction;

    @ApiModelProperty(value = "标签ids")
    private List<Long> folksonomyIds;
}


