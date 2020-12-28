package com.aquilaflycloud.mdc.param.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 订单退款入参
 */
@Data
@Accessors(chain = true)
public class OrderInfoWechatRefundParam implements Serializable {
    private static final long serialVersionUID = -2985287228833975979L;

    @ApiModelProperty(value = "OTA订单号", required = true)
    @NotBlank(message = "OTA订单号不能为空")
    private String otaOrderNo;

    @ApiModelProperty(value = "产品集合", required = true)
    @NotEmpty(message = "产品集合不能为空")
    private List<OrderRefundDetail> orderRefundDetailList;
}
