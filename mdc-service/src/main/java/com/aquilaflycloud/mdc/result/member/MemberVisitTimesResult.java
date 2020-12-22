package com.aquilaflycloud.mdc.result.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MemberVisitTimesResult {
    @ApiModelProperty(value = "日期")
    private List<String> date;

    @ApiModelProperty(value = "访次")
    private List<Long> visitTimes;

    @ApiModelProperty(value = "启动数")
    private List<Long> launch;

    public MemberVisitTimesResult(List<String> date, List<Long> visitTimes, List<Long> launch) {
        this.date = date;
        this.visitTimes = visitTimes;
        this.launch = launch;
    }

    public MemberVisitTimesResult() {
    }
}
