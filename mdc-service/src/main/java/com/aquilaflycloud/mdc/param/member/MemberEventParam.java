package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.EventTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class MemberEventParam {

    @ApiModelProperty(value = "业务数据id", required = true)
    @NotNull(message = "业务数据id不能为空")
    private Long businessId;

    @ApiModelProperty(value = "业务数据类型(member.BusinessTypeEnum)", required = true)
    @NotNull(message = "业务数据类型不能为空")
    private BusinessTypeEnum businessType;

    @ApiModelProperty(value = "事件类型(member.EventTypeEnum)", required = true)
    @NotNull(message = "事件类型不能为空")
    private EventTypeEnum eventType;
}