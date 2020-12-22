package com.aquilaflycloud.mdc.param.member;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * AlipayMemberDailyDataListParam
 *
 * @author Zengqingjie
 * @date 2020-05-25
 */
@Data
@Accessors(chain = true)
public class MemberDailyDataListParam {
    @ApiModelProperty(value = "日期(yyyy-MM-dd)")
    private String date;

    @ApiModelProperty(value = "appId")
    private String appId;
}
