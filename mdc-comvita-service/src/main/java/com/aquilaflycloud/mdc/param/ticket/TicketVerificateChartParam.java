package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author zhi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketVerificateChartParam extends AuthParam implements Serializable {

    @ApiModelProperty(value = "开始日期")
    private Date createTimeStart;

    @ApiModelProperty(value = "结束日期")
    private Date createTimeEnd;
}
