package com.aquilaflycloud.mdc.param.parking;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.parking.InvoiceTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * OrderGetParam
 *
 * @author star
 * @date 2020-02-10
 */
@AnotherFieldHasValue.List({
    @AnotherFieldHasValue(fieldName = "invoiceType", fieldValue = "COMPANY", notNullFieldName = "buyerNo", message = "购方税号不能为空"),
    @AnotherFieldHasValue(fieldName = "invoiceType", fieldValue = "COMPANY", notNullFieldName = "buyerAddress", message = "购方地址不能为空"),
    @AnotherFieldHasValue(fieldName = "invoiceType", fieldValue = "COMPANY", notNullFieldName = "buyerPhone", message = "购方电话不能为空"),
    @AnotherFieldHasValue(fieldName = "invoiceType", fieldValue = "COMPANY", notNullFieldName = "buyerBank", message = "购方开户行不能为空"),
    @AnotherFieldHasValue(fieldName = "invoiceType", fieldValue = "COMPANY", notNullFieldName = "buyerAccount", message = "购方账户不能为空"),
    @AnotherFieldHasValue(fieldName = "invoiceType", fieldValue = "COMPANY", notNullFieldName = "buyerName", message = "购方客户名称不能为空"),
})
@Data
@Accessors(chain = true)
public class InvoiceAddParam {
    @ApiModelProperty(value = "订单id", required = true)
    @NotNull(message = "订单id不能为空")
    private Long orderId;

    @ApiModelProperty(value = "开票类型", required = true)
    @NotNull(message = "开票类型不能为空")
    private InvoiceTypeEnum invoiceType;

    @ApiModelProperty(value = "购方税号")
    private String buyerNo;

    @ApiModelProperty(value = "购方地址")
    private String buyerAddress;

    @ApiModelProperty(value = "购方电话")
    private String buyerPhone;

    @ApiModelProperty(value = "购方开户行")
    private String buyerBank;

    @ApiModelProperty(value = "购方账户")
    private String buyerAccount;

    @ApiModelProperty(value = "购方客户名称")
    private String buyerName;
}
