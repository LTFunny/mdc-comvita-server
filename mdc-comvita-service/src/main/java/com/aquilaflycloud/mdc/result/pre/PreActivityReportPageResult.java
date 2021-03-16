package com.aquilaflycloud.mdc.result.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PreActivityReportPageResult
 * 预售活动导出分页返回结果
 * @Author linkq
 */
@Data
public class PreActivityReportPageResult {
    @ApiModelProperty(value = "活动Id")
    private Long activityId;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动时间")
    private String activityTime;

    @ApiModelProperty(value = "活动标签")
    private String activityFolksonomy;

    @ApiModelProperty(value = "参加人数")
    private Long participantsCount;

    @ApiModelProperty(value = "门店")
    private String orgName;

    @ApiModelProperty(value = "门店地址")
    private String orgAddress;

    @ApiModelProperty(value = "参加人姓名")
    private String participantName;

    @ApiModelProperty(value = "参加人性别")
    private String participantSex;

    @ApiModelProperty(value = "参加人生日")
    private String participantBirthdate;

    @ApiModelProperty(value = "参加人手机号码")
    private String participantPhoneNum;

    @ApiModelProperty(value = "地址信息")
    private String participantAddress;

}
