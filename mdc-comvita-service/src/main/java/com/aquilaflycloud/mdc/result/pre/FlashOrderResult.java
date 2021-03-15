package com.aquilaflycloud.mdc.result.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author zly
 */
@Data
public class FlashOrderResult {
    @ApiModelProperty(value = "订单id")
    private Long  id;

    @ApiModelProperty(value = "订单编号")
    private String orderCode;

    @ApiModelProperty(value = "收件人")
    private String reserveName;

    @ApiModelProperty(value = "收件地址")
    private String deliveryAddress;

    @ApiModelProperty(value = "创建时间时间")
    private Date createTime;

    @ApiModelProperty(value = "快递编码")
    private String expressCode;

    @ApiModelProperty(value = "快递单号")
    private String expressOrderCode;

    @ApiModelProperty(value = "快递名称")
    private String  expressName;
}
