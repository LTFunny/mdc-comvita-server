package com.aquilaflycloud.mdc.result.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * MemberSignContinueResult
 *
 * @author star
 * @date 2020/11/6
 */
@Data
public class MemberSignContinueResult implements Serializable {
    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "连续签到开始日期")
    private Date minDate;

    @ApiModelProperty(value = "连续签到结束日期")
    private Date maxDate;

    @ApiModelProperty(value = "连续签到天数")
    private Integer continueCount;

    @ApiModelProperty(value = "连续签到日期")
    private String dates;
}
