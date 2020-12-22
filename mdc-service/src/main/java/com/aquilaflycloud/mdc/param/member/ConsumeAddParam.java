package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class ConsumeAddParam {
    @ApiModelProperty(value="会员编码")
    private String memberCode;

    @ApiModelProperty(value="微信appId")
    private String wxAppId;

    @ApiModelProperty(value="微信openId")
    private String openId;

    @ApiModelProperty(value="微信unionId")
    private String unionId;

    @ApiModelProperty(value="支付宝appId")
    private String aliAppId;

    @ApiModelProperty(value="支付宝userId")
    private String userId;

    @ApiModelProperty(value="MDC商户号")
    private String shopId;

    @ApiModelProperty(value="商户编号")
    private String merchantNo;

    @ApiModelProperty(value="商户名称")
    private String merchantName;

    @ApiModelProperty(value="商品名称")
    private String productName;

    @ApiModelProperty(value="订单号")
    private String orderNo;

    @ApiModelProperty(value="流水号")
    private String payNo;

    @ApiModelProperty(value="银行卡号")
    private String creditCard;

    @ApiModelProperty(value="发卡银行")
    private String bankName;

    @ApiModelProperty(value="货币类型")
    private String moneyType;

    @ApiModelProperty(value="消费金额")
    private BigDecimal payMoney;

    @ApiModelProperty(value="消费时间")
    private Date payTime;

    @ApiModelProperty(value="支付方式")
    private String payType;

    @ApiModelProperty(value="操作类型")
    private String actionType;

    @ApiModelProperty(value="操作时间")
    private Date actionTime;

    @ApiModelProperty(value="第三方支付记录数据")
    private String allData;
}