package com.aquilaflycloud.mdc.extra.alipay.component;

import lombok.Data;

import java.io.Serializable;

@Data
public class AlipayConfig implements Serializable {
    private String protocol;

    private String gatewayHost;

    private String appId;

    private String signType;

    private String alipayPublicKey;

    private String merchantPrivateKey;

    private String merchantCertPath;

    private String alipayCertPath;

    private String alipayRootCertPath;

    private String notifyUrl;

    private String encryptKey;

    private static final long serialVersionUID = 1L;
}