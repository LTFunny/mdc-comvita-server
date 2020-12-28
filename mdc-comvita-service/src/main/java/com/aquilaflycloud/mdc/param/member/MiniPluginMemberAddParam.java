package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MiniPluginMemberAddParam implements Serializable {

    @ApiModelProperty(value = "插件小程序appId", required = true)
    @NotBlank(message = "插件小程序appId不能为空")
    private String pluginAppId;

    @ApiModelProperty(value = "小程序用户登录凭证", required = true)
    @NotBlank(message = "jsCode不能为空")
    private String jsCode;

    @ApiModelProperty(value = "不包括敏感信息的原始数据字符串，用于计算签名", required = true)
    @NotBlank(message = "rawData不能为空")
    private String rawData;

    @ApiModelProperty(value = "使用 sha1( rawData + sessionkey ) 得到字符串，用于校验用户信息", required = true)
    @NotBlank(message = "signature不能为空")
    private String signature;

    @ApiModelProperty(value = "包括敏感数据在内的完整用户信息的加密数据")
    private String encryptedData;

    @ApiModelProperty(value = "加密算法的初始向量")
    private String iv;

    @ApiModelProperty(value = "用户信息对象，不包含 openid 等敏感信息", required = true)
    @NotNull(message = "userInfo不能为空")
    private WechatUserInfo userInfo;
}
