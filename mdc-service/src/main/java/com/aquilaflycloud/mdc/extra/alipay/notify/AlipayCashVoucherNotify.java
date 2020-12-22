package com.aquilaflycloud.mdc.extra.alipay.notify;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AlipayCashVoucherNotify {
    @ApiModelProperty(value = "核销交易的支付宝业务号")
    private String alipayBizNo;

    @ApiModelProperty(value = "优惠券模板id")
    private String templateId;

    @ApiModelProperty(value = "业务发生时间")
    private String bizTime;

    @ApiModelProperty(value = "变动金额")
    private String fluxAmount;

    @ApiModelProperty(value = "被核销的券id")
    private String voucherId;

    @ApiModelProperty(value = "核销对应商户id")
    private String partnerId;

    @ApiModelProperty(value = "核销对应商户名称")
    private String partnerName;

    @ApiModelProperty(value = "支付宝用户id")
    private String userId;
}
