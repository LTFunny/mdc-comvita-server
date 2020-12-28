package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import com.aquilaflycloud.mdc.enums.member.SourceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * MemberAttributesAnalysisParam
 *
 * @author Zengqingjie
 * @date 2020-05-27
 */
@Data
@Accessors(chain = true)
public class MemberAttributesAnalysisParam {
    /*@ApiModelProperty(value = "开始日期(yyyy-MM-dd)", required = true)
    @NotBlank(message = "开始日期不能为空")
    private Date startDate;

    @ApiModelProperty(value = "结束日期(yyyy-MM-dd)", required = true)
    @NotBlank(message = "结束日期不能为空")
    private Date endDate;*/

    @ApiModelProperty(value = "appId", required = true)
    @NotBlank(message = "appId不能为空")
    private String appId;

    @ApiModelProperty(value = "奖励类型(member.RewardTypeEnum)", required = true)
    @NotNull(message = "奖励类型不能为空")
    private RewardTypeEnum rewardType;
}
