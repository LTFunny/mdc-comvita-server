package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@AnotherFieldHasValue(fieldName = "rankType", fieldValue = "ALL", canNullFieldName = "dateNum", message = "指定日期不能为空")
@Data
@Accessors(chain = true)
public class RewardRankListParam {
    @ApiModelProperty(value = "奖励类型(member.RewardTypeEnum)", required = true)
    @NotNull(message = "奖励类型不能为空")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value = "排名类型(ALL, MONTH, YEAR)", required = true)
    @NotNull(message = "排名类型不能为空")
    private RankTypeEnum rankType;

    @ApiModelProperty(value = "指定年份或月份,为空则为当前年份(yyyy)或月份(yyyy-MM)")
    private String dateNum;

    @ApiModelProperty(value = "返回排名数,最大1000,默认20")
    @Max(value = 1000, message = "不能超过1000")
    private Integer limit = 20;

    public enum RankTypeEnum {
        // 排名类型
        ALL, MONTH, YEAR
    }
}