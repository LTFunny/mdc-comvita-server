package com.aquilaflycloud.mdc.param.parking;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.parking.InvoiceTypeEnum;
import com.aquilaflycloud.mdc.model.parking.ParkingOrderInvoice;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * OrderGetParam
 *
 * @author star
 * @date 2020-02-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class InvoicePageParam extends PageParam<ParkingOrderInvoice> {
    @ApiModelProperty(value = "开票类型")
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
