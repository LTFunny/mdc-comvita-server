package com.aquilaflycloud.mdc.param.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class WechatTicketOrderCustInfoResult implements Serializable {
    @ApiModelProperty(value = "下单顾客名称")
    private String custName;

    @ApiModelProperty(value = "下单顾客手机号")
    private String mobile;
}
