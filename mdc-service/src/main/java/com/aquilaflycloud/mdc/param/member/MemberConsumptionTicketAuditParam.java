package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.mdc.enums.member.ConsumptionTicketStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class MemberConsumptionTicketAuditParam implements Serializable {
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "状态(member.ConsumptionTicketState)", required = true)
    @NotNull(message = "状态不能为空")
    private ConsumptionTicketStateEnum state;

    @ApiModelProperty(value = "审核反馈")
    private String feedbackContent;

    @ApiModelProperty(value = "消费金额(元)")
    private BigDecimal payMoney;

    @ApiModelProperty(value = "店铺id(存在于系统的店铺必传)")
    private Long shopId;

    @ApiModelProperty(value = "店铺名称")
    @NotBlank(message = "店铺名称不能为空")
    private String shopName;
}
