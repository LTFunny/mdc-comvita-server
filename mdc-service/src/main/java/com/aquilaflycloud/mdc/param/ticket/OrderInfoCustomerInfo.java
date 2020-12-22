package com.aquilaflycloud.mdc.param.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 订单顾客信息
 */
@Data
@Accessors(chain = true)
public class OrderInfoCustomerInfo implements Serializable {
    @ApiModelProperty(value = "游客名称", required = true)
    @NotNull(message = "游客名称不能为空")
    private String name;

    @ApiModelProperty(value = "游客手机号", required = true)
    @NotNull(message = "游客手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "游客身份证")
    private String cardId;

    @ApiModelProperty(value = "游客人脸url")
    private String customerImg;
}
