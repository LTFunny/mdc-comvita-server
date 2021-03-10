package com.aquilaflycloud.mdc.result.pre;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * PreFlashReportPageResult
 * 快闪活动导出分页返回结果
 * @Author linkq
 */
@Data
public class PreFlashReportPageResult {

    @ApiModelProperty(value = "活动Id")
    private Long activityId;

    @ApiModelProperty(value = "会员Id")
    private Long memberId;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动时间")
    private String activityTime;

    @ApiModelProperty(value = "活动标签")
    private String activityFolksonomy;

    @ApiModelProperty(value = "转发量")
    private Long sharePv = 0L;

    @ApiModelProperty(value = "点击量")
    private Long clickPv = 0L;

    @ApiModelProperty(value = "点击人数")
    private Long clickUv = 0L;

    @ApiModelProperty(value = "参加人数")
    private Long participantsCount;

    @ApiModelProperty(value = "参加转化率")
    private String conversionRate = NumberUtil.formatPercent(0, 2);

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

    @ApiModelProperty(value = "地址信息")
    private String participantAddress;

    @ApiModelProperty(value = "是否新会员")
    private String isNewMember;

    @ApiModelProperty(value = "是否曾经购买")
    private String isEverBought;

}
