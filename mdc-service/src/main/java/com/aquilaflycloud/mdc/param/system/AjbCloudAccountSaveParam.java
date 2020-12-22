package com.aquilaflycloud.mdc.param.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * AjbCloudAccountSaveParam
 *
 * @author star
 * @date 2020-01-07
 */
@Data
@Accessors(chain = true)
public class AjbCloudAccountSaveParam {
    @ApiModelProperty(value = "账号", required = true)
    @NotBlank(message = "账号不能为空")
    private String account;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "企业编号", required = true)
    @NotBlank(message = "企业编号不能为空")
    private String firmCode;

    @ApiModelProperty(value = "车场编号", required = true)
    @NotBlank(message = "车场编号不能为空")
    private String parkCode;

    @ApiModelProperty(value = "密钥", required = true)
    @NotBlank(message = "密钥不能为空")
    private String secret;

    @ApiModelProperty(value = "商户账号", required = true)
    @NotBlank(message = "商户账号不能为空")
    private String tenantsAccount;

    @ApiModelProperty(value = "商户密码", required = true)
    @NotBlank(message = "商户密码不能为空")
    private String tenantsPassword;

    @ApiModelProperty(value = "商户名称", required = true)
    @NotBlank(message = "商户名称不能为空")
    private String tenantsName;

    @ApiModelProperty(value = "联系人名", required = true)
    @NotBlank(message = "联系人名不能为空")
    private String linkMan;

    @ApiModelProperty(value = "联系方式", required = true)
    @NotBlank(message = "联系方式不能为空")
    private String mobilePhone;
}
