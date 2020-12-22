package com.aquilaflycloud.mdc.result.system;

import com.aquilaflycloud.mdc.enums.system.AccountSubTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class EasyPayAccountResult implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "支付账号类型")
    private AccountSubTypeEnum accountSubType;

    @ApiModelProperty(value = "商户编号")
    private String merchantNo;

    @ApiModelProperty(value = "签名类型")
    private SIGNTYPE signType;

    @ApiModelProperty(value = "md5密钥")
    private String md5Key;

    @ApiModelProperty(value = "商户rsa密钥")
    private String privateKey;

    @ApiModelProperty(value = "平台rsa公钥")
    private String publicKey;

    public enum SIGNTYPE {
        // 签名类型
        MD5, RSA
    }
}
