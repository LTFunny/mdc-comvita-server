package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.model.alipay.AlipayMemberDailyDataInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MemberGrowResult {
    @ApiModelProperty(value = "实时数据")
    private AlipayMemberDailyDataInfo currentInfo;

    @ApiModelProperty(value = "日期")
    private List<String> date;

    @ApiModelProperty(value = "活跃用户数")
    private List<Long> activeUser;

    @ApiModelProperty(value = "累加总用户数")
    private List<Long> totalUser;

    @ApiModelProperty(value = "访次")
    private List<Long> visitTimes;

    @ApiModelProperty(value = "启动数")
    private List<Long> launch;

    @ApiModelProperty(value = "新增用户数")
    private List<Long> newUser;

    @ApiModelProperty(value = "人均停留时长")
    private List<String> dailyDuration;

    @ApiModelProperty(value = "次均停留时长")
    private List<String> onceDuration;

    public MemberGrowResult(AlipayMemberDailyDataInfo currentInfo, List<String> date, List<Long> activeUser, List<Long> totalUser, List<Long> visitTimes, List<Long> launch, List<Long> newUser, List<String> dailyDuration, List<String> onceDuration) {
        this.currentInfo = currentInfo;
        this.date = date;
        this.activeUser = activeUser;
        this.totalUser = totalUser;
        this.visitTimes = visitTimes;
        this.launch = launch;
        this.newUser = newUser;
        this.dailyDuration = dailyDuration;
        this.onceDuration = onceDuration;
    }

    public MemberGrowResult() {
    }
}
