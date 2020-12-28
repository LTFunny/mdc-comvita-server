package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.model.ticket.TicketOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ChannelSalesOrderInfoParam extends PageAuthParam<TicketOrderInfo> implements Serializable {
    private static final long serialVersionUID = -5831675889082817884L;

    @ApiModelProperty(value = "渠道id")
    private Long id;

    @ApiModelProperty(value = "开始时间", required = true)
    @NotNull(message = "开始时间不能为空")
    private Date startDate;

    @ApiModelProperty(value = "结束时间", required = true)
    @NotNull(message = "结束时间不能为空")
    private Date endDate;
}
