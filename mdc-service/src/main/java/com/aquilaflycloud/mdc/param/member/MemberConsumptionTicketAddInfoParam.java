package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MemberConsumptionTicketAddInfoParam extends AuthParam {
    @ApiModelProperty(value = "小票凭证url", required = true)
    @NotBlank(message = "小票凭证url不能为空")
    private String ticketUrl;

    @ApiModelProperty(value = "支付凭证url")
    private String payUrl;

    @ApiModelProperty(value = "消费金额(元)", required = true)
    @NotNull(message = "消费金额不能为空")
    private BigDecimal payMoney;

    @ApiModelProperty(value = "店铺id(存在于系统的店铺必传)")
    private Long shopId;

    @ApiModelProperty(value = "店铺名称", required = true)
    @NotBlank(message = "店铺名称不能为空")
    private String shopName;
}
