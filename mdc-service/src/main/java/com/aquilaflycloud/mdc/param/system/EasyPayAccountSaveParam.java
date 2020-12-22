package com.aquilaflycloud.mdc.param.system;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.system.AccountSubTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * EasyPayAccountSaveParam
 *
 * @author star
 * @date 2019-12-06
 */
@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "signType", fieldValue = "MD5", notNullFieldName = "md5Key", message = "md5密钥不能为空"),
        @AnotherFieldHasValue(fieldName = "signType", fieldValue = "RSA", notNullFieldName = "privateKey", message = "商户rsa密钥不能为空"),
        @AnotherFieldHasValue(fieldName = "signType", fieldValue = "RSA", notNullFieldName = "publicKey", message = "平台rsa公钥不能为空")
})
@Data
@Accessors(chain = true)
public class EasyPayAccountSaveParam {
    @ApiModelProperty(value = "支付账号类型(system.AccountSubTypeEnum)", required = true)
    @NotNull(message = "支付账号类型")
    private AccountSubTypeEnum accountSubType;

    @ApiModelProperty(value = "商户编号", required = true)
    @NotNull(message = "商户编号不能为空")
    private String merchantNo;

    @ApiModelProperty(value = "签名类型(MD5, RSA)", required = true)
    @NotNull(message = "签名类型不能为空")
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
