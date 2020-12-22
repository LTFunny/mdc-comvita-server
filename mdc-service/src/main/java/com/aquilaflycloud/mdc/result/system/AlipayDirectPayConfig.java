package com.aquilaflycloud.mdc.result.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * AlipayDirectPayConfig
 *
 * @author star
 * @date 2020-04-09
 */
@Data
public class AlipayDirectPayConfig {
    @ApiModelProperty(value = "支付宝直联支付是否生效(默认false)")
    private Boolean effective;

    @ApiModelProperty(value = "支付宝服务商id")
    private String sysServiceProviderId;
}
