package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.EventTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@Accessors(chain = true)
public class MemberEventStatisticsParam {

    @ApiModelProperty(value = "业务数据id", required = true)
    @NotNull(message = "业务数据id不能为空")
    private Long businessId;

    @ApiModelProperty(value = "业务数据类型(member.BusinessTypeEnum)", required = true)
    @NotNull(message = "业务数据类型不能为空")
    private BusinessTypeEnum businessType;

    @ApiModelProperty(value = "事件类型(member.EventTypeEnum)")
    private Set<EventTypeEnum> eventTypes;

    @ApiModelProperty(value = "事件开始时间")
    private Date startTime;

    @ApiModelProperty(value = "事件结束时间")
    private Date endTime;
}