package com.aquilaflycloud.mdc.param.parking;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.parking.InvoiceTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * ParkingInvoicePageParam
 *
 * @author star
 * @date 2020-06-24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ParkingInvoicePageParam extends PageParam {
    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "微信或支付宝昵称")
    private String nickName;

    @ApiModelProperty(value = "车牌号")
    private String carNo;

    @ApiModelProperty(value = "车卡号")
    private String cardId;

    @ApiModelProperty(value = "支付时间开始")
    private Date payTimeStart;

    @ApiModelProperty(value = "支付时间结束")
    private Date payTimeEnd;

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

    @ApiModelProperty(value = "开票时间开始")
    private Date createTimeStart;

    @ApiModelProperty(value = "开票时间结束")
    private Date createTimeEnd;
}
