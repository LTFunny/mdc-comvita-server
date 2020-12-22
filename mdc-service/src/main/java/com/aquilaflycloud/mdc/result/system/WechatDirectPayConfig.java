package com.aquilaflycloud.mdc.result.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * WechatDirectPayConfig
 *
 * @author star
 * @date 2020/8/6
 */
@Data
public class WechatDirectPayConfig {
    @ApiModelProperty(value = "微信直联支付是否生效(默认false)")
    private Boolean effective;
}
