package com.aquilaflycloud.mdc.extra.alipay.notify;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AlipayCashVoucherPay {
    @ApiModelProperty(value = "外部业务单号")
    private String outRequestNo;

    @ApiModelProperty(value = "支付金额")
    private String amount;

    @ApiModelProperty(value = "资金订单号")
    private String authNo;

    @ApiModelProperty(value = "业务单号")
    private String operationId;

    @ApiModelProperty(value = "外部订单号")
    private String outOrderNo;

    @ApiModelProperty(value = "状态")
    private String status;
}
