package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.EventTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * MemberEventStatisticsResult
 *
 * @author star
 * @date 2019-11-21
 */
@Data
public class MemberEventStatisticsResult implements Serializable {
    @ApiModelProperty(value = "业务类型")
    private BusinessTypeEnum businessType;

    @ApiModelProperty(value = "业务id")
    private Long businessId;

    @ApiModelProperty(value = "事件类型")
    private EventTypeEnum eventType;

    @ApiModelProperty(value = "触发事件次数")
    private Long pv;

    @ApiModelProperty(value = "触发事件人数")
    private Long uv;

}
