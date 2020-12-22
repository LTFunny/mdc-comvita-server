package com.aquilaflycloud.mdc.result.system;

import com.aquilaflycloud.mdc.model.system.SystemTenantConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SystemTenantConfigResult extends SystemTenantConfig {
    @ApiModelProperty(value = "支付宝直联支付配置")
    private AlipayDirectPayConfig alipayDirectPayConfig;

    @ApiModelProperty(value = "微信直联支付配置")
    private WechatDirectPayConfig wechatDirectPayConfig;

    @ApiModelProperty(value = "通用授权应用配置")
    private AuthorUniversalConfig authorUniversalConfig;

    @ApiModelProperty(value = "统一会员配置")
    private UnifiedMemberConfig unifiedMemberConfig;
}
