package com.aquilaflycloud.mdc.param.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单创建入参
 */
@Data
@Accessors(chain = true)
public class OrderInfoWechatCreateParam implements Serializable {
    private static final long serialVersionUID = -5717014711870686914L;

    @ApiModelProperty(value = "景区id", required = true)
    @NotNull(message = "景区id不能为空")
    private Long scenicSpotId;

    @ApiModelProperty(value = "渠道id,有值需要传")
    private Long channelId;

    @ApiModelProperty(value = "产品集合", required = true)
    @NotEmpty(message = "产品集合不能为空")
    @Valid
    private List<OrderInfoDetailInfo> orderInfoDetailInfoList;

    @ApiModelProperty(value = "游玩时间", required = true)
    @NotNull(message = "游玩时间不能为空")
    private Date playDate;

    @ApiModelProperty(value = "总金额", required = true)
    @NotNull(message = "总金额不能为空")
    private BigDecimal amount;
}
