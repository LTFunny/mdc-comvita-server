package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.mdc.enums.easypay.PayType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * OrderPayParam
 *
 * @author star
 * @date 2020-03-25
 */
@Data
public class OrderPayParam implements Serializable {
    @ApiModelProperty(value = "订单id", required = true)
    @NotNull(message = "订单id不能为空")
    private Long orderId;

    @ApiModelProperty(value = "支付类型(WECHAT_MINI)", required = true)
    @NotNull(message = "支付类型不能为空")
    private PayType payType;
}
