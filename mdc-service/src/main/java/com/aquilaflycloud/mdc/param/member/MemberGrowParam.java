package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * AlipayMemberGrowParam
 *
 * @author Zengqingjie
 * @date 2020-05-26
 */
@Data
@Accessors(chain = true)
public class MemberGrowParam implements Serializable {
    @ApiModelProperty(value = "开始日期(yyyy-MM-dd)", required = true)
    @NotNull(message = "开始日期不能为空")
    private Date startDate;

    @ApiModelProperty(value = "结束日期(yyyy-MM-dd)", required = true)
    @NotNull(message = "结束日期不能为空")
    private Date endDate;

    @ApiModelProperty(value = "appId", required = true)
    @NotNull(message = "appId")
    private String appId;
}
