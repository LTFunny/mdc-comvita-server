package com.aquilaflycloud.mdc.param.alipay;

import com.aquilaflycloud.mdc.enums.alipay.SiteSourceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class AlipayAuthorSiteEditParam {
    @ApiModelProperty(value = "授权应用id", required = true)
    @NotNull(message = "授权应用id不能为空")
    private Long id;

    @ApiModelProperty(value="应用名")
    private String appName;

    @ApiModelProperty(value="授权类型(alipay.SiteSourceEnum)")
    private SiteSourceEnum source;

    @ApiModelProperty(value="商户密钥")
    private String merchantPrivateKey;

    @ApiModelProperty(value="支付宝公钥")
    private String alipayPublicKey;

    @ApiModelProperty(value = "加密串")
    private String encodingAesKey;
}
