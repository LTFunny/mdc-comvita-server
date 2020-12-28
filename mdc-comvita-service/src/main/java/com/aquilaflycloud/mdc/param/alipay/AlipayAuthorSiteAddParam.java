package com.aquilaflycloud.mdc.param.alipay;

import com.aquilaflycloud.mdc.enums.alipay.SiteSourceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class AlipayAuthorSiteAddParam {
    @ApiModelProperty(value = "支付宝appId", required = true)
    @NotBlank(message = "支付宝appId不能为空")
    private String alipayAppId;

    @ApiModelProperty(value="应用名", required = true)
    @NotBlank(message = "应用名不能为空")
    private String appName;

    @ApiModelProperty(value="授权类型(alipay.SiteSourceEnum)", required = true)
    @NotNull(message = "授权类型不能为空")
    private SiteSourceEnum source;

    @ApiModelProperty(value="商户密钥", required = true)
    @NotBlank(message = "商户密钥不能为空")
    private String merchantPrivateKey;

    @ApiModelProperty(value="支付宝公钥", required = true)
    @NotBlank(message = "支付宝公钥不能为空")
    private String alipayPublicKey;

    @ApiModelProperty(value = "加密串")
    private String encodingAesKey;
}
