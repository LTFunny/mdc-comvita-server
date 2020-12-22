package com.aquilaflycloud.mdc.extra.docom.component;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Zeng.qingjie
 * @description
 * @date 2019/10/28 16:15
 */
@Data
public class DoComConfig implements Serializable {
    private String baseUrl;
    private String merchantCode;
    private  String interfaceAccount;
    private  String secret;

    public DoComConfig() { }

    public DoComConfig(String merchantCode, String interfaceAccount, String secret, String baseUrl) {
        this.merchantCode = merchantCode;
        this.interfaceAccount = interfaceAccount;
        this.secret = secret;
        this.baseUrl = baseUrl;
    }
}
