package com.aquilaflycloud.mdc.param.easypay;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.easypay.FrpCodeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * OrderParam
 *
 * @author star
 * @date 2019-12-07
 */
@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "frpCode", fieldValues = {"ALIPAY_NATIVE", "ALIPAY_APP", "WEIXIN_APP", "JD_APP", "QQ_APP", "UNIONPAY_APP", "APPLET_PAY"}, notNullFieldName = "appId", message = "appId不能为空"),
        @AnotherFieldHasValue(fieldName = "frpCode", fieldValues = {"WEIXIN_GZH", "APPLET_PAY"}, notNullFieldName = "openId", message = "openId不能为空"),
        @AnotherFieldHasValue(fieldName = "frpCode", fieldValue = "ALIPAY_NATIVE", notNullFieldName = "userId", message = "userId不能为空"),
        @AnotherFieldHasValue(fieldName = "frpCode", fieldValues = {"ALIPAY_CARD", "WEIXIN_CARD", "JD_CARD", "QQ_CARD", "UNIONPAY_CARD"}, notNullFieldName = "authCode", message = "付款码数字不能为空")
})
@Data
@Accessors(chain = true)
public class OrderParam {
    @ApiModelProperty(value = "订单id", required = true)
    @NotNull(message = "订单id不能为空")
    private Long id;

    @ApiModelProperty(value = "订单号", required = true)
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @ApiModelProperty(value = "支付金额", required = true)
    @NotNull(message = "支付金额不能为空")
    private BigDecimal amount;

    @ApiModelProperty(value = "交易类型", required = true)
    @NotNull(message = "交易类型不能为空")
    private FrpCodeEnum frpCode;

    @ApiModelProperty(value = "商品名称", required = true)
    @NotNull(message = "商品名称不能为空")
    private String productName;

    @ApiModelProperty(value = "商品描述")
    private String productDesc;

    @ApiModelProperty(value = "公用回传参数")
    private String mp;

    @ApiModelProperty(value = "appId")
    private String appId;

    @ApiModelProperty(value = "微信openId")
    private String openId;

    @ApiModelProperty(value = "支付宝userId")
    private String userId;

    @ApiModelProperty(value = "付款码数字")
    private String authCode;

    @ApiModelProperty(value = "订单有效时间")
    private Integer timeExpire;
}


